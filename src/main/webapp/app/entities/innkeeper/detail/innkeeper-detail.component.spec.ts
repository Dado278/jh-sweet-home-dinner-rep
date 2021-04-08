import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { InnkeeperDetailComponent } from './innkeeper-detail.component';

describe('Component Tests', () => {
  describe('Innkeeper Management Detail Component', () => {
    let comp: InnkeeperDetailComponent;
    let fixture: ComponentFixture<InnkeeperDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InnkeeperDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ innkeeper: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InnkeeperDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InnkeeperDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load innkeeper on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.innkeeper).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
