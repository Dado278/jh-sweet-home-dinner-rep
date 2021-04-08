import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InnkeeperDetailComponent } from './innkeeper-detail.component';

describe('Component Tests', () => {
  describe('Innkeeper Management Detail Component', () => {
    let comp: InnkeeperDetailComponent;
    let fixture: ComponentFixture<InnkeeperDetailComponent>;

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
    });

    describe('OnInit', () => {
      it('Should load innkeeper on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.innkeeper).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
