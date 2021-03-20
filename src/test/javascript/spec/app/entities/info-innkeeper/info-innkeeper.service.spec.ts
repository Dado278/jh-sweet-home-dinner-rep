import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { InfoInnkeeperService } from 'app/entities/info-innkeeper/info-innkeeper.service';
import { IInfoInnkeeper, InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

describe('Service Tests', () => {
  describe('InfoInnkeeper Service', () => {
    let injector: TestBed;
    let service: InfoInnkeeperService;
    let httpMock: HttpTestingController;
    let elemDefault: IInfoInnkeeper;
    let expectedResult: IInfoInnkeeper | IInfoInnkeeper[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(InfoInnkeeperService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new InfoInnkeeper(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a InfoInnkeeper', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new InfoInnkeeper()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a InfoInnkeeper', () => {
        const returnedFromService = Object.assign(
          {
            nickname: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            services: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of InfoInnkeeper', () => {
        const returnedFromService = Object.assign(
          {
            nickname: 'BBBBBB',
            slogan: 'BBBBBB',
            description: 'BBBBBB',
            homePage: 'BBBBBB',
            latitude: 'BBBBBB',
            longitude: 'BBBBBB',
            address: 'BBBBBB',
            services: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a InfoInnkeeper', () => {
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
