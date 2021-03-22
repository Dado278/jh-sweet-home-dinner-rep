import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { TakeAwayUpdateComponent } from 'app/entities/take-away/take-away-update.component';
import { TakeAwayService } from 'app/entities/take-away/take-away.service';
import { TakeAway } from 'app/shared/model/take-away.model';

describe('Component Tests', () => {
  describe('TakeAway Management Update Component', () => {
    let comp: TakeAwayUpdateComponent;
    let fixture: ComponentFixture<TakeAwayUpdateComponent>;
    let service: TakeAwayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [TakeAwayUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TakeAwayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TakeAwayUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TakeAwayService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TakeAway(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TakeAway();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
