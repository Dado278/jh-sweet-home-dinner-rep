import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInfoInnkeeper, InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { InfoInnkeeperService } from './info-innkeeper.service';

@Component({
  selector: 'jhi-info-innkeeper-update',
  templateUrl: './info-innkeeper-update.component.html',
})
export class InfoInnkeeperUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nickname: [],
    slogan: [],
    description: [],
    homePage: [],
    latitude: [],
    longitude: [],
    address: [],
    services: [],
  });

  constructor(protected infoInnkeeperService: InfoInnkeeperService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infoInnkeeper }) => {
      this.updateForm(infoInnkeeper);
    });
  }

  updateForm(infoInnkeeper: IInfoInnkeeper): void {
    this.editForm.patchValue({
      id: infoInnkeeper.id,
      nickname: infoInnkeeper.nickname,
      slogan: infoInnkeeper.slogan,
      description: infoInnkeeper.description,
      homePage: infoInnkeeper.homePage,
      latitude: infoInnkeeper.latitude,
      longitude: infoInnkeeper.longitude,
      address: infoInnkeeper.address,
      services: infoInnkeeper.services,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infoInnkeeper = this.createFromForm();
    if (infoInnkeeper.id !== undefined) {
      this.subscribeToSaveResponse(this.infoInnkeeperService.update(infoInnkeeper));
    } else {
      this.subscribeToSaveResponse(this.infoInnkeeperService.create(infoInnkeeper));
    }
  }

  private createFromForm(): IInfoInnkeeper {
    return {
      ...new InfoInnkeeper(),
      id: this.editForm.get(['id'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
      description: this.editForm.get(['description'])!.value,
      homePage: this.editForm.get(['homePage'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      services: this.editForm.get(['services'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfoInnkeeper>>): void {
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
}
