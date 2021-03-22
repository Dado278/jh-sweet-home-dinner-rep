import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITakeAway, TakeAway } from 'app/shared/model/take-away.model';
import { TakeAwayService } from './take-away.service';
import { IInnkeeper } from 'app/shared/model/innkeeper.model';
import { InnkeeperService } from 'app/entities/innkeeper/innkeeper.service';

@Component({
  selector: 'jhi-take-away-update',
  templateUrl: './take-away-update.component.html',
})
export class TakeAwayUpdateComponent implements OnInit {
  isSaving = false;
  innkeepers: IInnkeeper[] = [];

  editForm = this.fb.group({
    id: [],
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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takeAway }) => {
      if (!takeAway.id) {
        const today = moment().startOf('day');
        takeAway.createDate = today;
        takeAway.updateDate = today;
      }

      this.updateForm(takeAway);

      this.innkeeperService.query().subscribe((res: HttpResponse<IInnkeeper[]>) => (this.innkeepers = res.body || []));
    });
  }

  updateForm(takeAway: ITakeAway): void {
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

  private createFromForm(): ITakeAway {
    return {
      ...new TakeAway(),
      id: this.editForm.get(['id'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? moment(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITakeAway>>): void {
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

  trackById(index: number, item: IInnkeeper): any {
    return item.id;
  }
}
