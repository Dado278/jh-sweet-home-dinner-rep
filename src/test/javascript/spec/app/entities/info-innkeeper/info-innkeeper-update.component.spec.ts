import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { InfoInnkeeperUpdateComponent } from 'app/entities/info-innkeeper/info-innkeeper-update.component';
import { InfoInnkeeperService } from 'app/entities/info-innkeeper/info-innkeeper.service';
import { InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

describe('Component Tests', () => {
  describe('InfoInnkeeper Management Update Component', () => {
    let comp: InfoInnkeeperUpdateComponent;
    let fixture: ComponentFixture<InfoInnkeeperUpdateComponent>;
    let service: InfoInnkeeperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [InfoInnkeeperUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InfoInnkeeperUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InfoInnkeeperUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InfoInnkeeperService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InfoInnkeeper(123);
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
        const entity = new InfoInnkeeper();
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
