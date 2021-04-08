import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { ICustomer, Customer } from '../customer.model';

import { CustomerService } from './customer.service';

describe('Service Tests', () => {
  describe('Customer Service', () => {
    let service: CustomerService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomer;
    let expectedResult: ICustomer | ICustomer[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nickname: 'AAAAAAA',
        freshman: 0,
        email: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        gender: Gender.M,
        createDate: currentDate,
        updateDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Customer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Customer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Customer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nickname: 'BBBBBB',
            freshman: 1,
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Customer', () => {
        const patchObject = Object.assign(
          {
            freshman: 1,
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Customer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Customer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nickname: 'BBBBBB',
            freshman: 1,
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Customer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomerToCollectionIfMissing', () => {
        it('should add a Customer to an empty array', () => {
          const customer: ICustomer = { id: 123 };
          expectedResult = service.addCustomerToCollectionIfMissing([], customer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customer);
        });

        it('should not add a Customer to an array that contains it', () => {
          const customer: ICustomer = { id: 123 };
          const customerCollection: ICustomer[] = [
            {
              ...customer,
            },
            { id: 456 },
          ];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, customer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Customer to an array that doesn't contain it", () => {
          const customer: ICustomer = { id: 123 };
          const customerCollection: ICustomer[] = [{ id: 456 }];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, customer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customer);
        });

        it('should add only unique Customer to an array', () => {
          const customerArray: ICustomer[] = [{ id: 123 }, { id: 456 }, { id: 45363 }];
          const customerCollection: ICustomer[] = [{ id: 123 }];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, ...customerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customer: ICustomer = { id: 123 };
          const customer2: ICustomer = { id: 456 };
          expectedResult = service.addCustomerToCollectionIfMissing([], customer, customer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customer);
          expect(expectedResult).toContain(customer2);
        });

        it('should accept null and undefined values', () => {
          const customer: ICustomer = { id: 123 };
          expectedResult = service.addCustomerToCollectionIfMissing([], null, customer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customer);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
