import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhSweetHomeDinnerApplicationTestModule } from '../../../test.module';
import { InnkeeperDetailComponent } from 'app/entities/innkeeper/innkeeper-detail.component';
import { Innkeeper } from 'app/shared/model/innkeeper.model';

describe('Component Tests', () => {
  describe('Innkeeper Management Detail Component', () => {
    let comp: InnkeeperDetailComponent;
    let fixture: ComponentFixture<InnkeeperDetailComponent>;
    const route = ({ data: of({ innkeeper: new Innkeeper(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhSweetHomeDinnerApplicationTestModule],
        declarations: [InnkeeperDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
