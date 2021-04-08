import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SharedDinnerDetailComponent } from './shared-dinner-detail.component';

describe('Component Tests', () => {
  describe('SharedDinner Management Detail Component', () => {
    let comp: SharedDinnerDetailComponent;
    let fixture: ComponentFixture<SharedDinnerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SharedDinnerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sharedDinner: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SharedDinnerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SharedDinnerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sharedDinner on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sharedDinner).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
