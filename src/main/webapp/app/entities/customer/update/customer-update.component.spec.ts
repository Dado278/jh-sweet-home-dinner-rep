jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomerService } from '../service/customer.service';
import { ICustomer, Customer } from '../customer.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISharedDinner } from 'app/entities/shared-dinner/shared-dinner.model';
import { SharedDinnerService } from 'app/entities/shared-dinner/service/shared-dinner.service';

import { CustomerUpdateComponent } from './customer-update.component';

describe('Component Tests', () => {
  describe('Customer Management Update Component', () => {
    let comp: CustomerUpdateComponent;
    let fixture: ComponentFixture<CustomerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customerService: CustomerService;
    let userService: UserService;
    let sharedDinnerService: SharedDinnerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customerService = TestBed.inject(CustomerService);
      userService = TestBed.inject(UserService);
      sharedDinnerService = TestBed.inject(SharedDinnerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const customer: ICustomer = { id: 456 };
        const internalUser: IUser = { id: 27699 };
        customer.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SharedDinner query and add missing value', () => {
        const customer: ICustomer = { id: 456 };
        const sharedDinners: ISharedDinner[] = [{ id: 22233 }];
        customer.sharedDinners = sharedDinners;

        const sharedDinnerCollection: ISharedDinner[] = [{ id: 22611 }];
        spyOn(sharedDinnerService, 'query').and.returnValue(of(new HttpResponse({ body: sharedDinnerCollection })));
        const additionalSharedDinners = [...sharedDinners];
        const expectedCollection: ISharedDinner[] = [...additionalSharedDinners, ...sharedDinnerCollection];
        spyOn(sharedDinnerService, 'addSharedDinnerToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(sharedDinnerService.query).toHaveBeenCalled();
        expect(sharedDinnerService.addSharedDinnerToCollectionIfMissing).toHaveBeenCalledWith(
          sharedDinnerCollection,
          ...additionalSharedDinners
        );
        expect(comp.sharedDinnersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const customer: ICustomer = { id: 456 };
        const internalUser: IUser = { id: 47918 };
        customer.internalUser = internalUser;
        const sharedDinners: ISharedDinner = { id: 31679 };
        customer.sharedDinners = [sharedDinners];

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(customer));
        expect(comp.usersSharedCollection).toContain(internalUser);
        expect(comp.sharedDinnersSharedCollection).toContain(sharedDinners);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = { id: 123 };
        spyOn(customerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(customerService.update).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = new Customer();
        spyOn(customerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customer }));
        saveSubject.complete();

        // THEN
        expect(customerService.create).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = { id: 123 };
        spyOn(customerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(customerService.update).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSharedDinnerById', () => {
        it('Should return tracked SharedDinner primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSharedDinnerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedSharedDinner', () => {
        it('Should return option if no SharedDinner is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedSharedDinner(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected SharedDinner for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedSharedDinner(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this SharedDinner is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedSharedDinner(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
