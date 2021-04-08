jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TakeAwayService } from '../service/take-away.service';
import { ITakeAway, TakeAway } from '../take-away.model';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';
import { InnkeeperService } from 'app/entities/innkeeper/service/innkeeper.service';

import { TakeAwayUpdateComponent } from './take-away-update.component';

describe('Component Tests', () => {
  describe('TakeAway Management Update Component', () => {
    let comp: TakeAwayUpdateComponent;
    let fixture: ComponentFixture<TakeAwayUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let takeAwayService: TakeAwayService;
    let innkeeperService: InnkeeperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TakeAwayUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TakeAwayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TakeAwayUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      takeAwayService = TestBed.inject(TakeAwayService);
      innkeeperService = TestBed.inject(InnkeeperService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Innkeeper query and add missing value', () => {
        const takeAway: ITakeAway = { id: 456 };
        const innkeeper: IInnkeeper = { id: 52948 };
        takeAway.innkeeper = innkeeper;

        const innkeeperCollection: IInnkeeper[] = [{ id: 48539 }];
        spyOn(innkeeperService, 'query').and.returnValue(of(new HttpResponse({ body: innkeeperCollection })));
        const additionalInnkeepers = [innkeeper];
        const expectedCollection: IInnkeeper[] = [...additionalInnkeepers, ...innkeeperCollection];
        spyOn(innkeeperService, 'addInnkeeperToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ takeAway });
        comp.ngOnInit();

        expect(innkeeperService.query).toHaveBeenCalled();
        expect(innkeeperService.addInnkeeperToCollectionIfMissing).toHaveBeenCalledWith(innkeeperCollection, ...additionalInnkeepers);
        expect(comp.innkeepersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const takeAway: ITakeAway = { id: 456 };
        const innkeeper: IInnkeeper = { id: 58516 };
        takeAway.innkeeper = innkeeper;

        activatedRoute.data = of({ takeAway });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(takeAway));
        expect(comp.innkeepersSharedCollection).toContain(innkeeper);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const takeAway = { id: 123 };
        spyOn(takeAwayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ takeAway });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: takeAway }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(takeAwayService.update).toHaveBeenCalledWith(takeAway);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const takeAway = new TakeAway();
        spyOn(takeAwayService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ takeAway });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: takeAway }));
        saveSubject.complete();

        // THEN
        expect(takeAwayService.create).toHaveBeenCalledWith(takeAway);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const takeAway = { id: 123 };
        spyOn(takeAwayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ takeAway });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(takeAwayService.update).toHaveBeenCalledWith(takeAway);
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
