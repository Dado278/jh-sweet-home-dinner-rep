jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITakeAway, TakeAway } from '../take-away.model';
import { TakeAwayService } from '../service/take-away.service';

import { TakeAwayRoutingResolveService } from './take-away-routing-resolve.service';

describe('Service Tests', () => {
  describe('TakeAway routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TakeAwayRoutingResolveService;
    let service: TakeAwayService;
    let resultTakeAway: ITakeAway | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TakeAwayRoutingResolveService);
      service = TestBed.inject(TakeAwayService);
      resultTakeAway = undefined;
    });

    describe('resolve', () => {
      it('should return ITakeAway returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTakeAway = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTakeAway).toEqual({ id: 123 });
      });

      it('should return new ITakeAway if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTakeAway = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTakeAway).toEqual(new TakeAway());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTakeAway = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTakeAway).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
