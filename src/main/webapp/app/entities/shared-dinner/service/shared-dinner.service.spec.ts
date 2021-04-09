import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISharedDinner, SharedDinner } from '../shared-dinner.model';

import { SharedDinnerService } from './shared-dinner.service';

describe('Service Tests', () => {
  describe('SharedDinner Service', () => {
    let service: SharedDinnerService;
    let httpMock: HttpTestingController;
    let elemDefault: ISharedDinner;
    let expectedResult: ISharedDinner | ISharedDinner[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SharedDinnerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        createDate: currentDate,
        updateDate: currentDate,
        title: 'AAAAAAA',
        slogan: 'AAAAAAA',
        description: 'AAAAAAA',
        dinnerDate: currentDate,
        homePage: 'AAAAAAA',
        latitude: 'AAAAAAA',
        longitude: 'AAAAAAA',
        address: 'AAAAAAA',
        costmin: 0,
        costmax: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            dinnerDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SharedDinner', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            dinnerDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
            dinnerDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SharedDinner()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SharedDinner', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            dinnerDate: currentDate.format(DATE_FORMAT),
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            costmin: 1,
            costmax: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
            dinnerDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SharedDinner', () => {
        const patchObject = Object.assign(
          {
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            slogan: 'BBBBBB',
            dinnerDate: currentDate.format(DATE_FORMAT),
            latitude: 'BBBBBB',
            address: 'BBBBBB',
          },
          new SharedDinner()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
            dinnerDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SharedDinner', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            dinnerDate: currentDate.format(DATE_FORMAT),
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            costmin: 1,
            costmax: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            updateDate: currentDate,
            dinnerDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SharedDinner', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSharedDinnerToCollectionIfMissing', () => {
        it('should add a SharedDinner to an empty array', () => {
          const sharedDinner: ISharedDinner = { id: 123 };
          expectedResult = service.addSharedDinnerToCollectionIfMissing([], sharedDinner);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sharedDinner);
        });

        it('should not add a SharedDinner to an array that contains it', () => {
          const sharedDinner: ISharedDinner = { id: 123 };
          const sharedDinnerCollection: ISharedDinner[] = [
            {
              ...sharedDinner,
            },
            { id: 456 },
          ];
          expectedResult = service.addSharedDinnerToCollectionIfMissing(sharedDinnerCollection, sharedDinner);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SharedDinner to an array that doesn't contain it", () => {
          const sharedDinner: ISharedDinner = { id: 123 };
          const sharedDinnerCollection: ISharedDinner[] = [{ id: 456 }];
          expectedResult = service.addSharedDinnerToCollectionIfMissing(sharedDinnerCollection, sharedDinner);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sharedDinner);
        });

        it('should add only unique SharedDinner to an array', () => {
          const sharedDinnerArray: ISharedDinner[] = [{ id: 123 }, { id: 456 }, { id: 29946 }];
          const sharedDinnerCollection: ISharedDinner[] = [{ id: 123 }];
          expectedResult = service.addSharedDinnerToCollectionIfMissing(sharedDinnerCollection, ...sharedDinnerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sharedDinner: ISharedDinner = { id: 123 };
          const sharedDinner2: ISharedDinner = { id: 456 };
          expectedResult = service.addSharedDinnerToCollectionIfMissing([], sharedDinner, sharedDinner2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sharedDinner);
          expect(expectedResult).toContain(sharedDinner2);
        });

        it('should accept null and undefined values', () => {
          const sharedDinner: ISharedDinner = { id: 123 };
          expectedResult = service.addSharedDinnerToCollectionIfMissing([], null, sharedDinner, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sharedDinner);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
