import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { InfoInnkeeperComponent } from 'app/entities/info-innkeeper/info-innkeeper.component';
import { InfoInnkeeperService } from 'app/entities/info-innkeeper/info-innkeeper.service';
import { InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

describe('Component Tests', () => {
  describe('InfoInnkeeper Management Component', () => {
    let comp: InfoInnkeeperComponent;
    let fixture: ComponentFixture<InfoInnkeeperComponent>;
    let service: InfoInnkeeperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [InfoInnkeeperComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(InfoInnkeeperComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InfoInnkeeperComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InfoInnkeeperService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InfoInnkeeper(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.infoInnkeepers && comp.infoInnkeepers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InfoInnkeeper(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.infoInnkeepers && comp.infoInnkeepers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
