import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICustomer, Customer } from '../customer.model';
import { CustomerService } from '../service/customer.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISharedDinner } from 'app/entities/shared-dinner/shared-dinner.model';
import { SharedDinnerService } from 'app/entities/shared-dinner/service/shared-dinner.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  sharedDinnersSharedCollection: ISharedDinner[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    nickname: [null, [Validators.required, Validators.pattern('^[a-zA-Z0-9_-]{3,16}$')]],
    avatarImageBlob: [null, []],
    avatarImageBlobContentType: [],
    avatarTextBlob: [],
    freshman: [null, []],
    email: [null, [Validators.pattern('^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$')]],
    phoneNumber: [null, [Validators.required, Validators.pattern('^[0-9]{9,15}$')]],
    gender: [],
    createDate: [],
    updateDate: [],
    internalUser: [],
    sharedDinners: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected customerService: CustomerService,
    protected userService: UserService,
    protected sharedDinnerService: SharedDinnerService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      if (customer.id === undefined) {
        const today = dayjs().startOf('day');
        customer.createDate = today;
        customer.updateDate = today;
      }

      this.updateForm(customer);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhSweetHomeDinnerApplicationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackSharedDinnerById(index: number, item: ISharedDinner): number {
    return item.id!;
  }

  getSelectedSharedDinner(option: ISharedDinner, selectedVals?: ISharedDinner[]): ISharedDinner {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.editForm.patchValue({
      id: customer.id,
      nickname: customer.nickname,
      avatarImageBlob: customer.avatarImageBlob,
      avatarImageBlobContentType: customer.avatarImageBlobContentType,
      avatarTextBlob: customer.avatarTextBlob,
      freshman: customer.freshman,
      email: customer.email,
      phoneNumber: customer.phoneNumber,
      gender: customer.gender,
      createDate: customer.createDate ? customer.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: customer.updateDate ? customer.updateDate.format(DATE_TIME_FORMAT) : null,
      internalUser: customer.internalUser,
      sharedDinners: customer.sharedDinners,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, customer.internalUser);
    this.sharedDinnersSharedCollection = this.sharedDinnerService.addSharedDinnerToCollectionIfMissing(
      this.sharedDinnersSharedCollection,
      ...(customer.sharedDinners ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.sharedDinnerService
      .query()
      .pipe(map((res: HttpResponse<ISharedDinner[]>) => res.body ?? []))
      .pipe(
        map((sharedDinners: ISharedDinner[]) =>
          this.sharedDinnerService.addSharedDinnerToCollectionIfMissing(sharedDinners, ...(this.editForm.get('sharedDinners')!.value ?? []))
        )
      )
      .subscribe((sharedDinners: ISharedDinner[]) => (this.sharedDinnersSharedCollection = sharedDinners));
  }

  protected createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      avatarImageBlobContentType: this.editForm.get(['avatarImageBlobContentType'])!.value,
      avatarImageBlob: this.editForm.get(['avatarImageBlob'])!.value,
      avatarTextBlob: this.editForm.get(['avatarTextBlob'])!.value,
      freshman: this.editForm.get(['freshman'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      internalUser: this.editForm.get(['internalUser'])!.value,
      sharedDinners: this.editForm.get(['sharedDinners'])!.value,
    };
  }
}
