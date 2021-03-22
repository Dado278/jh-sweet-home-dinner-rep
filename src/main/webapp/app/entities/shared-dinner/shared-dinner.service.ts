import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISharedDinner } from 'app/shared/model/shared-dinner.model';

type EntityResponseType = HttpResponse<ISharedDinner>;
type EntityArrayResponseType = HttpResponse<ISharedDinner[]>;

@Injectable({ providedIn: 'root' })
export class SharedDinnerService {
  public resourceUrl = SERVER_API_URL + 'api/shared-dinners';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/shared-dinners';

  constructor(protected http: HttpClient) {}

  create(sharedDinner: ISharedDinner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sharedDinner);
    return this.http
      .post<ISharedDinner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sharedDinner: ISharedDinner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sharedDinner);
    return this.http
      .put<ISharedDinner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISharedDinner>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISharedDinner[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISharedDinner[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(sharedDinner: ISharedDinner): ISharedDinner {
    const copy: ISharedDinner = Object.assign({}, sharedDinner, {
      createDate: sharedDinner.createDate && sharedDinner.createDate.isValid() ? sharedDinner.createDate.toJSON() : undefined,
      updateDate: sharedDinner.updateDate && sharedDinner.updateDate.isValid() ? sharedDinner.updateDate.toJSON() : undefined,
      dinnerDate: sharedDinner.dinnerDate && sharedDinner.dinnerDate.isValid() ? sharedDinner.dinnerDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? moment(res.body.createDate) : undefined;
      res.body.updateDate = res.body.updateDate ? moment(res.body.updateDate) : undefined;
      res.body.dinnerDate = res.body.dinnerDate ? moment(res.body.dinnerDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sharedDinner: ISharedDinner) => {
        sharedDinner.createDate = sharedDinner.createDate ? moment(sharedDinner.createDate) : undefined;
        sharedDinner.updateDate = sharedDinner.updateDate ? moment(sharedDinner.updateDate) : undefined;
        sharedDinner.dinnerDate = sharedDinner.dinnerDate ? moment(sharedDinner.dinnerDate) : undefined;
      });
    }
    return res;
  }
}
