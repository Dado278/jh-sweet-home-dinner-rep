import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { IInnkeeper, Innkeeper } from '../innkeeper.model';

import { InnkeeperService } from './innkeeper.service';

describe('Service Tests', () => {
  describe('Innkeeper Service', () => {
    let service: InnkeeperService;
    let httpMock: HttpTestingController;
    let elemDefault: IInnkeeper;
    let expectedResult: IInnkeeper | IInnkeeper[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InnkeeperService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nickname: 'AAAAAAA',
        avatarImageBlobContentType: 'image/png',
        avatarImageBlob: 'AAAAAAA',
        avatarTextBlob: 'AAAAAAA',
        freshman: 0,
        email: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        gender: Gender.M,
        slogan: 'AAAAAAA',
        description: 'AAAAAAA',
        homePage: 'AAAAAAA',
        latitude: 'AAAAAAA',
        longitude: 'AAAAAAA',
        address: 'AAAAAAA',
        services: 'AAAAAAA',
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
            id: 1,
            nickname: 'BBBBBB',
            avatarImageBlob: 'BBBBBB',
            avatarTextBlob: 'BBBBBB',
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

      it('should partial update a Innkeeper', () => {
        const patchObject = Object.assign(
          {
            nickname: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            services: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updateDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Innkeeper()
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

      it('should return a list of Innkeeper', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nickname: 'BBBBBB',
            avatarImageBlob: 'BBBBBB',
            avatarTextBlob: 'BBBBBB',
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

      describe('addInnkeeperToCollectionIfMissing', () => {
        it('should add a Innkeeper to an empty array', () => {
          const innkeeper: IInnkeeper = { id: 123 };
          expectedResult = service.addInnkeeperToCollectionIfMissing([], innkeeper);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(innkeeper);
        });

        it('should not add a Innkeeper to an array that contains it', () => {
          const innkeeper: IInnkeeper = { id: 123 };
          const innkeeperCollection: IInnkeeper[] = [
            {
              ...innkeeper,
            },
            { id: 456 },
          ];
          expectedResult = service.addInnkeeperToCollectionIfMissing(innkeeperCollection, innkeeper);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Innkeeper to an array that doesn't contain it", () => {
          const innkeeper: IInnkeeper = { id: 123 };
          const innkeeperCollection: IInnkeeper[] = [{ id: 456 }];
          expectedResult = service.addInnkeeperToCollectionIfMissing(innkeeperCollection, innkeeper);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(innkeeper);
        });

        it('should add only unique Innkeeper to an array', () => {
          const innkeeperArray: IInnkeeper[] = [{ id: 123 }, { id: 456 }, { id: 17670 }];
          const innkeeperCollection: IInnkeeper[] = [{ id: 123 }];
          expectedResult = service.addInnkeeperToCollectionIfMissing(innkeeperCollection, ...innkeeperArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const innkeeper: IInnkeeper = { id: 123 };
          const innkeeper2: IInnkeeper = { id: 456 };
          expectedResult = service.addInnkeeperToCollectionIfMissing([], innkeeper, innkeeper2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(innkeeper);
          expect(expectedResult).toContain(innkeeper2);
        });

        it('should accept null and undefined values', () => {
          const innkeeper: IInnkeeper = { id: 123 };
          expectedResult = service.addInnkeeperToCollectionIfMissing([], null, innkeeper, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(innkeeper);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
