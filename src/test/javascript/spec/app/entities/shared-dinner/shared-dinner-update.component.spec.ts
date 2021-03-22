import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { SharedDinnerUpdateComponent } from 'app/entities/shared-dinner/shared-dinner-update.component';
import { SharedDinnerService } from 'app/entities/shared-dinner/shared-dinner.service';
import { SharedDinner } from 'app/shared/model/shared-dinner.model';

describe('Component Tests', () => {
  describe('SharedDinner Management Update Component', () => {
    let comp: SharedDinnerUpdateComponent;
    let fixture: ComponentFixture<SharedDinnerUpdateComponent>;
    let service: SharedDinnerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [SharedDinnerUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SharedDinnerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SharedDinnerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SharedDinnerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SharedDinner(123);
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
        const entity = new SharedDinner();
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
