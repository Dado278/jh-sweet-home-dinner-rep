import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISharedDinner, SharedDinner } from 'app/shared/model/shared-dinner.model';
import { SharedDinnerService } from './shared-dinner.service';
import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { InfoInnkeeperService } from 'app/entities/info-innkeeper/info-innkeeper.service';

@Component({
  selector: 'jhi-shared-dinner-update',
  templateUrl: './shared-dinner-update.component.html',
})
export class SharedDinnerUpdateComponent implements OnInit {
  isSaving = false;
  infoinnkeepers: IInfoInnkeeper[] = [];

  editForm = this.fb.group({
    id: [],
    createDate: [],
    updateDate: [],
    title: [],
    slogan: [],
    description: [],
    homePage: [],
    latitude: [],
    longitude: [],
    address: [],
    costmin: [],
    costmax: [],
    infoInnkeeper: [],
  });

  constructor(
    protected sharedDinnerService: SharedDinnerService,
    protected infoInnkeeperService: InfoInnkeeperService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sharedDinner }) => {
      if (!sharedDinner.id) {
        const today = moment().startOf('day');
        sharedDinner.createDate = today;
        sharedDinner.updateDate = today;
      }

      this.updateForm(sharedDinner);

      this.infoInnkeeperService.query().subscribe((res: HttpResponse<IInfoInnkeeper[]>) => (this.infoinnkeepers = res.body || []));
    });
  }

  updateForm(sharedDinner: ISharedDinner): void {
    this.editForm.patchValue({
      id: sharedDinner.id,
      createDate: sharedDinner.createDate ? sharedDinner.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: sharedDinner.updateDate ? sharedDinner.updateDate.format(DATE_TIME_FORMAT) : null,
      title: sharedDinner.title,
      slogan: sharedDinner.slogan,
      description: sharedDinner.description,
      homePage: sharedDinner.homePage,
      latitude: sharedDinner.latitude,
      longitude: sharedDinner.longitude,
      address: sharedDinner.address,
      costmin: sharedDinner.costmin,
      costmax: sharedDinner.costmax,
      infoInnkeeper: sharedDinner.infoInnkeeper,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sharedDinner = this.createFromForm();
    if (sharedDinner.id !== undefined) {
      this.subscribeToSaveResponse(this.sharedDinnerService.update(sharedDinner));
    } else {
      this.subscribeToSaveResponse(this.sharedDinnerService.create(sharedDinner));
    }
  }

  private createFromForm(): ISharedDinner {
    return {
      ...new SharedDinner(),
      id: this.editForm.get(['id'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? moment(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      title: this.editForm.get(['title'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
      description: this.editForm.get(['description'])!.value,
      homePage: this.editForm.get(['homePage'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      costmin: this.editForm.get(['costmin'])!.value,
      costmax: this.editForm.get(['costmax'])!.value,
      infoInnkeeper: this.editForm.get(['infoInnkeeper'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISharedDinner>>): void {
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
