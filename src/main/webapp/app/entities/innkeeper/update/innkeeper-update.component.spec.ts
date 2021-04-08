jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InnkeeperService } from '../service/innkeeper.service';
import { IInnkeeper, Innkeeper } from '../innkeeper.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { InnkeeperUpdateComponent } from './innkeeper-update.component';

describe('Component Tests', () => {
  describe('Innkeeper Management Update Component', () => {
    let comp: InnkeeperUpdateComponent;
    let fixture: ComponentFixture<InnkeeperUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let innkeeperService: InnkeeperService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InnkeeperUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InnkeeperUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InnkeeperUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      innkeeperService = TestBed.inject(InnkeeperService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const innkeeper: IInnkeeper = { id: 456 };
        const internalUser: IUser = { id: 13820 };
        innkeeper.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 8136 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ innkeeper });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const innkeeper: IInnkeeper = { id: 456 };
        const internalUser: IUser = { id: 62330 };
        innkeeper.internalUser = internalUser;

        activatedRoute.data = of({ innkeeper });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(innkeeper));
        expect(comp.usersSharedCollection).toContain(internalUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const innkeeper = { id: 123 };
        spyOn(innkeeperService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ innkeeper });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: innkeeper }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(innkeeperService.update).toHaveBeenCalledWith(innkeeper);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const innkeeper = new Innkeeper();
        spyOn(innkeeperService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ innkeeper });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: innkeeper }));
        saveSubject.complete();

        // THEN
        expect(innkeeperService.create).toHaveBeenCalledWith(innkeeper);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const innkeeper = { id: 123 };
        spyOn(innkeeperService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ innkeeper });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(innkeeperService.update).toHaveBeenCalledWith(innkeeper);
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
    });
  });
});
