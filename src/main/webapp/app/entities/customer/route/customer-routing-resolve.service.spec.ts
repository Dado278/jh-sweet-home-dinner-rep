jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICustomer, Customer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

import { CustomerRoutingResolveService } from './customer-routing-resolve.service';

describe('Service Tests', () => {
  describe('Customer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CustomerRoutingResolveService;
    let service: CustomerService;
    let resultCustomer: ICustomer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CustomerRoutingResolveService);
      service = TestBed.inject(CustomerService);
      resultCustomer = undefined;
    });

    describe('resolve', () => {
      it('should return ICustomer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCustomer).toEqual({ id: 123 });
      });

      it('should return new ICustomer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCustomer).toEqual(new Customer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCustomer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
