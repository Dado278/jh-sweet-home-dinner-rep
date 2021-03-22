import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IInnkeeper, Innkeeper } from 'app/shared/model/innkeeper.model';
import { InnkeeperService } from './innkeeper.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-innkeeper-update',
  templateUrl: './innkeeper-update.component.html',
})
export class InnkeeperUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nickname: [null, [Validators.required, Validators.pattern('^[a-z0-9_-]{3,16}$')]],
    freshman: [],
    email: [null, [Validators.required, Validators.pattern('^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$')]],
    phoneNumber: [null, [Validators.required, Validators.pattern('^[0-9]{9,15}$')]],
    gender: [],
    slogan: [],
    description: [null, [Validators.required]],
    homePage: [null, [Validators.pattern('^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$')]],
    latitude: [],
    longitude: [],
    address: [null, [Validators.required]],
    services: [],
    createDate: [],
    updateDate: [],
    internalUser: [],
  });

  constructor(
    protected innkeeperService: InnkeeperService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ innkeeper }) => {
      if (!innkeeper.id) {
        const today = moment().startOf('day');
        innkeeper.createDate = today;
        innkeeper.updateDate = today;
      }

      this.updateForm(innkeeper);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(innkeeper: IInnkeeper): void {
    this.editForm.patchValue({
      id: innkeeper.id,
      nickname: innkeeper.nickname,
      freshman: innkeeper.freshman,
      email: innkeeper.email,
      phoneNumber: innkeeper.phoneNumber,
      gender: innkeeper.gender,
      slogan: innkeeper.slogan,
      description: innkeeper.description,
      homePage: innkeeper.homePage,
      latitude: innkeeper.latitude,
      longitude: innkeeper.longitude,
      address: innkeeper.address,
      services: innkeeper.services,
      createDate: innkeeper.createDate ? innkeeper.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: innkeeper.updateDate ? innkeeper.updateDate.format(DATE_TIME_FORMAT) : null,
      internalUser: innkeeper.internalUser,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const innkeeper = this.createFromForm();
    if (innkeeper.id !== undefined) {
      this.subscribeToSaveResponse(this.innkeeperService.update(innkeeper));
    } else {
      this.subscribeToSaveResponse(this.innkeeperService.create(innkeeper));
    }
  }

  private createFromForm(): IInnkeeper {
    return {
      ...new Innkeeper(),
      id: this.editForm.get(['id'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      freshman: this.editForm.get(['freshman'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
      description: this.editForm.get(['description'])!.value,
      homePage: this.editForm.get(['homePage'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      services: this.editForm.get(['services'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? moment(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInnkeeper>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
