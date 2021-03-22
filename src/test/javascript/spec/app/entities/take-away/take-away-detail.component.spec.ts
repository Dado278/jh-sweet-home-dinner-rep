import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { TakeAwayDetailComponent } from 'app/entities/take-away/take-away-detail.component';
import { TakeAway } from 'app/shared/model/take-away.model';

describe('Component Tests', () => {
  describe('TakeAway Management Detail Component', () => {
    let comp: TakeAwayDetailComponent;
    let fixture: ComponentFixture<TakeAwayDetailComponent>;
    const route = ({ data: of({ takeAway: new TakeAway(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [TakeAwayDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TakeAwayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TakeAwayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load takeAway on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.takeAway).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
