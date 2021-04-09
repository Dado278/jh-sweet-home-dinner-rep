import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITakeAway, TakeAway } from '../take-away.model';

import { TakeAwayService } from './take-away.service';

describe('Service Tests', () => {
  describe('TakeAway Service', () => {
    let service: TakeAwayService;
    let httpMock: HttpTestingController;
    let elemDefault: ITakeAway;
    let expectedResult: ITakeAway | ITakeAway[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TakeAwayService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        createDate: currentDate,
        updateDate: currentDate,
        dish: 'AAAAAAA',
        description: 'AAAAAAA',
        ingredients: 'AAAAAAA',
        allergens: 'AAAAAAA',
        latitude: 'AAAAAAA',
        longitude: 'AAAAAAA',
        address: 'AAAAAAA',
        costmin: 0,
        costmax: 0,
        tags: 'AAAAAAA',
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

      it('should create a TakeAway', () => {
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

        service.create(new TakeAway()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TakeAway', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            dish: 'BBBBBB',
            description: 'BBBBBB',
            ingredients: 'BBBBBB',
            allergens: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            costmin: 1,
            costmax: 1,
            tags: 'BBBBBB',
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

      it('should partial update a TakeAway', () => {
        const patchObject = Object.assign(
          {
            createDate: currentDate.format(DATE_TIME_FORMAT),
            dish: 'BBBBBB',
            description: 'BBBBBB',
            latitude: 'BBBBBB',
            costmax: 1,
          },
          new TakeAway()
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

      it('should return a list of TakeAway', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
            dish: 'BBBBBB',
            description: 'BBBBBB',
            ingredients: 'BBBBBB',
            allergens: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            costmin: 1,
            costmax: 1,
            tags: 'BBBBBB',
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

      it('should delete a TakeAway', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTakeAwayToCollectionIfMissing', () => {
        it('should add a TakeAway to an empty array', () => {
          const takeAway: ITakeAway = { id: 123 };
          expectedResult = service.addTakeAwayToCollectionIfMissing([], takeAway);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(takeAway);
        });

        it('should not add a TakeAway to an array that contains it', () => {
          const takeAway: ITakeAway = { id: 123 };
          const takeAwayCollection: ITakeAway[] = [
            {
              ...takeAway,
            },
            { id: 456 },
          ];
          expectedResult = service.addTakeAwayToCollectionIfMissing(takeAwayCollection, takeAway);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TakeAway to an array that doesn't contain it", () => {
          const takeAway: ITakeAway = { id: 123 };
          const takeAwayCollection: ITakeAway[] = [{ id: 456 }];
          expectedResult = service.addTakeAwayToCollectionIfMissing(takeAwayCollection, takeAway);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(takeAway);
        });

        it('should add only unique TakeAway to an array', () => {
          const takeAwayArray: ITakeAway[] = [{ id: 123 }, { id: 456 }, { id: 83530 }];
          const takeAwayCollection: ITakeAway[] = [{ id: 123 }];
          expectedResult = service.addTakeAwayToCollectionIfMissing(takeAwayCollection, ...takeAwayArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const takeAway: ITakeAway = { id: 123 };
          const takeAway2: ITakeAway = { id: 456 };
          expectedResult = service.addTakeAwayToCollectionIfMissing([], takeAway, takeAway2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(takeAway);
          expect(expectedResult).toContain(takeAway2);
        });

        it('should accept null and undefined values', () => {
          const takeAway: ITakeAway = { id: 123 };
          expectedResult = service.addTakeAwayToCollectionIfMissing([], null, takeAway, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(takeAway);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
