import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { InnkeeperService } from 'app/entities/innkeeper/innkeeper.service';
import { IInnkeeper, Innkeeper } from 'app/shared/model/innkeeper.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

describe('Service Tests', () => {
  describe('Innkeeper Service', () => {
    let injector: TestBed;
    let service: InnkeeperService;
    let httpMock: HttpTestingController;
    let elemDefault: IInnkeeper;
    let expectedResult: IInnkeeper | IInnkeeper[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(InnkeeperService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Innkeeper(
        0,
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        Gender.M,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate
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

      it('should create a Innkeeper', () => {
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

        service.create(new Innkeeper()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Innkeeper', () => {
        const returnedFromService = Object.assign(
          {
            nickname: 'BBBBBB',
            freshman: 1,
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            services: 'BBBBBB',
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

      it('should return a list of Innkeeper', () => {
        const returnedFromService = Object.assign(
          {
            nickname: 'BBBBBB',
            freshman: 1,
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            services: 'BBBBBB',
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

      it('should delete a Innkeeper', () => {
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
