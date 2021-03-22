import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { SharedDinnerDetailComponent } from 'app/entities/shared-dinner/shared-dinner-detail.component';
import { SharedDinner } from 'app/shared/model/shared-dinner.model';

describe('Component Tests', () => {
  describe('SharedDinner Management Detail Component', () => {
    let comp: SharedDinnerDetailComponent;
    let fixture: ComponentFixture<SharedDinnerDetailComponent>;
    const route = ({ data: of({ sharedDinner: new SharedDinner(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [SharedDinnerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
