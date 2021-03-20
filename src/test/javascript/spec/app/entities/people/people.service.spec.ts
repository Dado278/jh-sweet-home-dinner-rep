import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PeopleService } from 'app/entities/people/people.service';
import { IPeople, People } from 'app/shared/model/people.model';
import { PersonType } from 'app/shared/model/enumerations/person-type.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

describe('Service Tests', () => {
  describe('People Service', () => {
    let injector: TestBed;
    let service: PeopleService;
    let httpMock: HttpTestingController;
    let elemDefault: IPeople;
    let expectedResult: IPeople | IPeople[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PeopleService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new People(
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        PersonType.CUSTOMER,
        'AAAAAAA',
        'AAAAAAA',
        Gender.M,
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

      it('should create a People', () => {
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

        service.create(new People()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a People', () => {
        const returnedFromService = Object.assign(
          {
            username: 'BBBBBB',
            password: 'BBBBBB',
            freshman: 1,
            personType: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            token: 'BBBBBB',
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

      it('should return a list of People', () => {
        const returnedFromService = Object.assign(
          {
            username: 'BBBBBB',
            password: 'BBBBBB',
            freshman: 1,
            personType: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            gender: 'BBBBBB',
            token: 'BBBBBB',
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

      it('should delete a People', () => {
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
