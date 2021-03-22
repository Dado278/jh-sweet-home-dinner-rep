import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { InnkeeperUpdateComponent } from 'app/entities/innkeeper/innkeeper-update.component';
import { InnkeeperService } from 'app/entities/innkeeper/innkeeper.service';
import { Innkeeper } from 'app/shared/model/innkeeper.model';

describe('Component Tests', () => {
  describe('Innkeeper Management Update Component', () => {
    let comp: InnkeeperUpdateComponent;
    let fixture: ComponentFixture<InnkeeperUpdateComponent>;
    let service: InnkeeperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [InnkeeperUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InnkeeperUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InnkeeperUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InnkeeperService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Innkeeper(123);
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
        const entity = new Innkeeper();
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
