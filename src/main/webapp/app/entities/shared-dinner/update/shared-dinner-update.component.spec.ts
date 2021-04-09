jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SharedDinnerService } from '../service/shared-dinner.service';
import { ISharedDinner, SharedDinner } from '../shared-dinner.model';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';
import { InnkeeperService } from 'app/entities/innkeeper/service/innkeeper.service';

import { SharedDinnerUpdateComponent } from './shared-dinner-update.component';

describe('Component Tests', () => {
  describe('SharedDinner Management Update Component', () => {
    let comp: SharedDinnerUpdateComponent;
    let fixture: ComponentFixture<SharedDinnerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sharedDinnerService: SharedDinnerService;
    let innkeeperService: InnkeeperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SharedDinnerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SharedDinnerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SharedDinnerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sharedDinnerService = TestBed.inject(SharedDinnerService);
      innkeeperService = TestBed.inject(InnkeeperService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Innkeeper query and add missing value', () => {
        const sharedDinner: ISharedDinner = { id: 456 };
        const innkeeper: IInnkeeper = { id: 87110 };
        sharedDinner.innkeeper = innkeeper;

        const innkeeperCollection: IInnkeeper[] = [{ id: 17670 }];
        spyOn(innkeeperService, 'query').and.returnValue(of(new HttpResponse({ body: innkeeperCollection })));
        const additionalInnkeepers = [innkeeper];
        const expectedCollection: IInnkeeper[] = [...additionalInnkeepers, ...innkeeperCollection];
        spyOn(innkeeperService, 'addInnkeeperToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ sharedDinner });
        comp.ngOnInit();

        expect(innkeeperService.query).toHaveBeenCalled();
        expect(innkeeperService.addInnkeeperToCollectionIfMissing).toHaveBeenCalledWith(innkeeperCollection, ...additionalInnkeepers);
        expect(comp.innkeepersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sharedDinner: ISharedDinner = { id: 456 };
        const innkeeper: IInnkeeper = { id: 572 };
        sharedDinner.innkeeper = innkeeper;

        activatedRoute.data = of({ sharedDinner });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sharedDinner));
        expect(comp.innkeepersSharedCollection).toContain(innkeeper);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sharedDinner = { id: 123 };
        spyOn(sharedDinnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sharedDinner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sharedDinner }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sharedDinnerService.update).toHaveBeenCalledWith(sharedDinner);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sharedDinner = new SharedDinner();
        spyOn(sharedDinnerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sharedDinner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sharedDinner }));
        saveSubject.complete();

        // THEN
        expect(sharedDinnerService.create).toHaveBeenCalledWith(sharedDinner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sharedDinner = { id: 123 };
        spyOn(sharedDinnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sharedDinner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sharedDinnerService.update).toHaveBeenCalledWith(sharedDinner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInnkeeperById', () => {
        it('Should return tracked Innkeeper primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInnkeeperById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
