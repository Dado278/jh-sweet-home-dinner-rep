import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInnkeeper, Innkeeper } from '../innkeeper.model';
import { InnkeeperService } from '../service/innkeeper.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-innkeeper-update',
  templateUrl: './innkeeper-update.component.html',
})
export class InnkeeperUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    nickname: [null, [Validators.required, Validators.pattern('^[A-Za-z0-9_-]{3,16}$')]],
    avatarImageBlob: [null, []],
    avatarImageBlobContentType: [],
    avatarTextBlob: [],
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
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected innkeeperService: InnkeeperService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ innkeeper }) => {
      if (innkeeper.id === undefined) {
        const today = dayjs().startOf('day');
        innkeeper.createDate = today;
        innkeeper.updateDate = today;
      }

      this.updateForm(innkeeper);

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
    const innkeeper = this.createFromForm();
    if (innkeeper.id !== undefined) {
      this.subscribeToSaveResponse(this.innkeeperService.update(innkeeper));
    } else {
      this.subscribeToSaveResponse(this.innkeeperService.create(innkeeper));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInnkeeper>>): void {
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

  protected updateForm(innkeeper: IInnkeeper): void {
    this.editForm.patchValue({
      id: innkeeper.id,
      nickname: innkeeper.nickname,
      avatarImageBlob: innkeeper.avatarImageBlob,
      avatarImageBlobContentType: innkeeper.avatarImageBlobContentType,
      avatarTextBlob: innkeeper.avatarTextBlob,
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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, innkeeper.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IInnkeeper {
    return {
      ...new Innkeeper(),
      id: this.editForm.get(['id'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      avatarImageBlobContentType: this.editForm.get(['avatarImageBlobContentType'])!.value,
      avatarImageBlob: this.editForm.get(['avatarImageBlob'])!.value,
      avatarTextBlob: this.editForm.get(['avatarTextBlob'])!.value,
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
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }
}
