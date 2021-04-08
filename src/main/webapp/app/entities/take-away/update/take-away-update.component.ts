import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITakeAway, TakeAway } from '../take-away.model';
import { TakeAwayService } from '../service/take-away.service';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';
import { InnkeeperService } from 'app/entities/innkeeper/service/innkeeper.service';

@Component({
  selector: 'jhi-take-away-update',
  templateUrl: './take-away-update.component.html',
})
export class TakeAwayUpdateComponent implements OnInit {
  isSaving = false;

  innkeepersSharedCollection: IInnkeeper[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    createDate: [],
    updateDate: [],
    dish: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    description: [null, [Validators.required]],
    ingredients: [null, [Validators.required]],
    allergens: [null, [Validators.required]],
    latitude: [],
    longitude: [],
    address: [null, [Validators.required]],
    costmin: [null, [Validators.required]],
    costmax: [null, [Validators.required]],
    tags: [],
    innkeeper: [],
  });

  constructor(
    protected takeAwayService: TakeAwayService,
    protected innkeeperService: InnkeeperService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takeAway }) => {
      if (takeAway.id === undefined) {
        const today = dayjs().startOf('day');
        takeAway.createDate = today;
        takeAway.updateDate = today;
      }

      this.updateForm(takeAway);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const takeAway = this.createFromForm();
    if (takeAway.id !== undefined) {
      this.subscribeToSaveResponse(this.takeAwayService.update(takeAway));
    } else {
      this.subscribeToSaveResponse(this.takeAwayService.create(takeAway));
    }
  }

  trackInnkeeperById(index: number, item: IInnkeeper): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITakeAway>>): void {
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

  protected updateForm(takeAway: ITakeAway): void {
    this.editForm.patchValue({
      id: takeAway.id,
      createDate: takeAway.createDate ? takeAway.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: takeAway.updateDate ? takeAway.updateDate.format(DATE_TIME_FORMAT) : null,
      dish: takeAway.dish,
      description: takeAway.description,
      ingredients: takeAway.ingredients,
      allergens: takeAway.allergens,
      latitude: takeAway.latitude,
      longitude: takeAway.longitude,
      address: takeAway.address,
      costmin: takeAway.costmin,
      costmax: takeAway.costmax,
      tags: takeAway.tags,
      innkeeper: takeAway.innkeeper,
    });

    this.innkeepersSharedCollection = this.innkeeperService.addInnkeeperToCollectionIfMissing(
      this.innkeepersSharedCollection,
      takeAway.innkeeper
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

  protected createFromForm(): ITakeAway {
    return {
      ...new TakeAway(),
      id: this.editForm.get(['id'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      dish: this.editForm.get(['dish'])!.value,
      description: this.editForm.get(['description'])!.value,
      ingredients: this.editForm.get(['ingredients'])!.value,
      allergens: this.editForm.get(['allergens'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      costmin: this.editForm.get(['costmin'])!.value,
      costmax: this.editForm.get(['costmax'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      innkeeper: this.editForm.get(['innkeeper'])!.value,
    };
  }
}
