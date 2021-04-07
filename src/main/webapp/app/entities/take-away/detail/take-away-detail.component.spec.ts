import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TakeAwayDetailComponent } from './take-away-detail.component';

describe('Component Tests', () => {
  describe('TakeAway Management Detail Component', () => {
    let comp: TakeAwayDetailComponent;
    let fixture: ComponentFixture<TakeAwayDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TakeAwayDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ takeAway: { id: 123 } }) },
          },
        ],
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
