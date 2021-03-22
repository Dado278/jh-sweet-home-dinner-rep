import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ISharedDinner } from 'app/shared/model/shared-dinner.model';
import { SharedDinnerService } from 'app/entities/shared-dinner/shared-dinner.service';

type SelectableEntity = IUser | ISharedDinner;

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  shareddinners: ISharedDinner[] = [];

  editForm = this.fb.group({
    id: [],
    nickname: [null, [Validators.required, Validators.pattern('^[a-z0-9_-]{3,16}$')]],
    freshman: [],
    email: [null, [Validators.pattern('^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$')]],
    phoneNumber: [null, [Validators.required, Validators.pattern('^[0-9]{9,15}$')]],
    gender: [],
    createDate: [],
    updateDate: [],
    internalUser: [],
    sharedDinners: [],
  });

  constructor(
    protected customerService: CustomerService,
    protected userService: UserService,
    protected sharedDinnerService: SharedDinnerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      if (!customer.id) {
        const today = moment().startOf('day');
        customer.createDate = today;
        customer.updateDate = today;
      }

      this.updateForm(customer);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.sharedDinnerService.query().subscribe((res: HttpResponse<ISharedDinner[]>) => (this.shareddinners = res.body || []));
    });
  }

  updateForm(customer: ICustomer): void {
    this.editForm.patchValue({
      id: customer.id,
      nickname: customer.nickname,
      freshman: customer.freshman,
      email: customer.email,
      phoneNumber: customer.phoneNumber,
      gender: customer.gender,
      createDate: customer.createDate ? customer.createDate.format(DATE_TIME_FORMAT) : null,
      updateDate: customer.updateDate ? customer.updateDate.format(DATE_TIME_FORMAT) : null,
      internalUser: customer.internalUser,
      sharedDinners: customer.sharedDinners,
    });
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

  private createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      freshman: this.editForm.get(['freshman'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? moment(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      internalUser: this.editForm.get(['internalUser'])!.value,
      sharedDinners: this.editForm.get(['sharedDinners'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: ISharedDinner[], option: ISharedDinner): ISharedDinner {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
