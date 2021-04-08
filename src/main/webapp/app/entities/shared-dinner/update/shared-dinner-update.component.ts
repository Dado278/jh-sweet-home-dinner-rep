import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISharedDinner, SharedDinner } from '../shared-dinner.model';
import { SharedDinnerService } from '../service/shared-dinner.service';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';
import { InnkeeperService } from 'app/entities/innkeeper/service/innkeeper.service';

@Component({
  selector: 'jhi-shared-dinner-update',
  templateUrl: './shared-dinner-update.component.html',
})
export class SharedDinnerUpdateComponent implements OnInit {
  isSaving = false;

  innkeepersSharedCollection: IInnkeeper[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    createDate: [],
    updateDate: [],
    title: [null, [Validators.required]],
    slogan: [],
    description: [null, [Validators.required]],
    dinnerDate: [],
    homePage: [null, [Validators.pattern('^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$')]],
    latitude: [],
    longitude: [],
    address: [null, [Validators.required]],
    costmin: [null, [Validators.required]],
    costmax: [null, [Validators.required]],
    innkeeper: [],
  });

  constructor(
    protected sharedDinnerService: SharedDinnerService,
    protected innkeeperService: InnkeeperService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sharedDinner }) => {
      if (sharedDinner.id === undefined) {
        const today = dayjs().startOf('day');
        sharedDinner.createDate = today;
        sharedDinner.updateDate = today;
      }

      this.updateForm(sharedDinner);

      this.loadRelationshipsOptions();
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

  trackInnkeeperById(index: number, item: IInnkeeper): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISharedDinner>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sharedDinner: ISharedDinner): void {
    this.editForm.patchValue({
      id: sharedDinner.id,
      createDate: sharedDinner.createDate ? sharedDinner.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: sharedDinner.updateDate ? sharedDinner.updateDate.format(DATE_TIME_FORMAT) : null,
      title: sharedDinner.title,
      slogan: sharedDinner.slogan,
      description: sharedDinner.description,
      dinnerDate: sharedDinner.dinnerDate,
      homePage: sharedDinner.homePage,
      latitude: sharedDinner.latitude,
      longitude: sharedDinner.longitude,
      address: sharedDinner.address,
      costmin: sharedDinner.costmin,
      costmax: sharedDinner.costmax,
      innkeeper: sharedDinner.innkeeper,
    });

    this.innkeepersSharedCollection = this.innkeeperService.addInnkeeperToCollectionIfMissing(
      this.innkeepersSharedCollection,
      sharedDinner.innkeeper
    );
  }

  protected loadRelationshipsOptions(): void {
    this.innkeeperService
      .query()
      .pipe(map((res: HttpResponse<IInnkeeper[]>) => res.body ?? []))
      .pipe(
        map((innkeepers: IInnkeeper[]) =>
          this.innkeeperService.addInnkeeperToCollectionIfMissing(innkeepers, this.editForm.get('innkeeper')!.value)
        )
      )
      .subscribe((innkeepers: IInnkeeper[]) => (this.innkeepersSharedCollection = innkeepers));
  }

  protected createFromForm(): ISharedDinner {
    return {
      ...new SharedDinner(),
      id: this.editForm.get(['id'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      title: this.editForm.get(['title'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
      description: this.editForm.get(['description'])!.value,
      dinnerDate: this.editForm.get(['dinnerDate'])!.value,
      homePage: this.editForm.get(['homePage'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      costmin: this.editForm.get(['costmin'])!.value,
      costmax: this.editForm.get(['costmax'])!.value,
      innkeeper: this.editForm.get(['innkeeper'])!.value,
    };
  }
}
