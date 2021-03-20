import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPeople, People } from 'app/shared/model/people.model';
import { PeopleService } from './people.service';
import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { InfoInnkeeperService } from 'app/entities/info-innkeeper/info-innkeeper.service';

@Component({
  selector: 'jhi-people-update',
  templateUrl: './people-update.component.html',
})
export class PeopleUpdateComponent implements OnInit {
  isSaving = false;
  infoinnkeepers: IInfoInnkeeper[] = [];

  editForm = this.fb.group({
    id: [],
    username: [],
    password: [],
    freshman: [],
    personType: [],
    email: [],
    phoneNumber: [],
    gender: [],
    token: [],
    createDate: [],
    updateDate: [],
    infoInnkeeper: [],
  });

  constructor(
    protected peopleService: PeopleService,
    protected infoInnkeeperService: InfoInnkeeperService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ people }) => {
      if (!people.id) {
        const today = moment().startOf('day');
        people.createDate = today;
        people.updateDate = today;
      }

      this.updateForm(people);

      this.infoInnkeeperService
        .query({ filter: 'people-is-null' })
        .pipe(
          map((res: HttpResponse<IInfoInnkeeper[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IInfoInnkeeper[]) => {
          if (!people.infoInnkeeper || !people.infoInnkeeper.id) {
            this.infoinnkeepers = resBody;
          } else {
            this.infoInnkeeperService
              .find(people.infoInnkeeper.id)
              .pipe(
                map((subRes: HttpResponse<IInfoInnkeeper>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IInfoInnkeeper[]) => (this.infoinnkeepers = concatRes));
          }
        });
    });
  }

  updateForm(people: IPeople): void {
    this.editForm.patchValue({
      id: people.id,
      username: people.username,
      password: people.password,
      freshman: people.freshman,
      personType: people.personType,
      email: people.email,
      phoneNumber: people.phoneNumber,
      gender: people.gender,
      token: people.token,
      createDate: people.createDate ? people.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: people.updateDate ? people.updateDate.format(DATE_TIME_FORMAT) : null,
      infoInnkeeper: people.infoInnkeeper,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const people = this.createFromForm();
    if (people.id !== undefined) {
      this.subscribeToSaveResponse(this.peopleService.update(people));
    } else {
      this.subscribeToSaveResponse(this.peopleService.create(people));
    }
  }

  private createFromForm(): IPeople {
    return {
      ...new People(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      freshman: this.editForm.get(['freshman'])!.value,
      personType: this.editForm.get(['personType'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      token: this.editForm.get(['token'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? moment(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      infoInnkeeper: this.editForm.get(['infoInnkeeper'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeople>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IInfoInnkeeper): any {
    return item.id;
  }
}
