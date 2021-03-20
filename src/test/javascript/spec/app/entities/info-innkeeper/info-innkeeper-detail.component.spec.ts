import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { InfoInnkeeperDetailComponent } from 'app/entities/info-innkeeper/info-innkeeper-detail.component';
import { InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

describe('Component Tests', () => {
  describe('InfoInnkeeper Management Detail Component', () => {
    let comp: InfoInnkeeperDetailComponent;
    let fixture: ComponentFixture<InfoInnkeeperDetailComponent>;
    const route = ({ data: of({ infoInnkeeper: new InfoInnkeeper(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [InfoInnkeeperDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InfoInnkeeperDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InfoInnkeeperDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load infoInnkeeper on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.infoInnkeeper).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
