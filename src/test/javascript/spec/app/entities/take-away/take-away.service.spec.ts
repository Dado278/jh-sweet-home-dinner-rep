import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { TakeAwayService } from 'app/entities/take-away/take-away.service';
import { ITakeAway, TakeAway } from 'app/shared/model/take-away.model';

describe('Service Tests', () => {
  describe('TakeAway Service', () => {
    let injector: TestBed;
    let service: TakeAwayService;
    let httpMock: HttpTestingController;
    let elemDefault: ITakeAway;
    let expectedResult: ITakeAway | ITakeAway[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TakeAwayService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TakeAway(
        0,
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA'
      );
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

      it('should return a list of TakeAway', () => {
        const returnedFromService = Object.assign(
          {
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
