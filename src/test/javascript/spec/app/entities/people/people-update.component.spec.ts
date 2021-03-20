import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { PeopleUpdateComponent } from 'app/entities/people/people-update.component';
import { PeopleService } from 'app/entities/people/people.service';
import { People } from 'app/shared/model/people.model';

describe('Component Tests', () => {
  describe('People Management Update Component', () => {
    let comp: PeopleUpdateComponent;
    let fixture: ComponentFixture<PeopleUpdateComponent>;
    let service: PeopleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [PeopleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PeopleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeopleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeopleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new People(123);
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
        const entity = new People();
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
