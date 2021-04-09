jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInnkeeper, Innkeeper } from '../innkeeper.model';
import { InnkeeperService } from '../service/innkeeper.service';

import { InnkeeperRoutingResolveService } from './innkeeper-routing-resolve.service';

describe('Service Tests', () => {
  describe('Innkeeper routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InnkeeperRoutingResolveService;
    let service: InnkeeperService;
    let resultInnkeeper: IInnkeeper | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InnkeeperRoutingResolveService);
      service = TestBed.inject(InnkeeperService);
      resultInnkeeper = undefined;
    });

    describe('resolve', () => {
      it('should return IInnkeeper returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInnkeeper = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInnkeeper).toEqual({ id: 123 });
      });

      it('should return new IInnkeeper if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInnkeeper = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInnkeeper).toEqual(new Innkeeper());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInnkeeper = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInnkeeper).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
