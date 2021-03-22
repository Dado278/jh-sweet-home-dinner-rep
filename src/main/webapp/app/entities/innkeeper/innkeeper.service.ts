import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IInnkeeper } from 'app/shared/model/innkeeper.model';

type EntityResponseType = HttpResponse<IInnkeeper>;
type EntityArrayResponseType = HttpResponse<IInnkeeper[]>;

@Injectable({ providedIn: 'root' })
export class InnkeeperService {
  public resourceUrl = SERVER_API_URL + 'api/innkeepers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/innkeepers';

  constructor(protected http: HttpClient) {}

  create(innkeeper: IInnkeeper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(innkeeper);
    return this.http
      .post<IInnkeeper>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(innkeeper: IInnkeeper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(innkeeper);
    return this.http
      .put<IInnkeeper>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInnkeeper>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInnkeeper[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInnkeeper[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(innkeeper: IInnkeeper): IInnkeeper {
    const copy: IInnkeeper = Object.assign({}, innkeeper, {
      createDate: innkeeper.createDate && innkeeper.createDate.isValid() ? innkeeper.createDate.toJSON() : undefined,
      updateDate: innkeeper.updateDate && innkeeper.updateDate.isValid() ? innkeeper.updateDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? moment(res.body.createDate) : undefined;
      res.body.updateDate = res.body.updateDate ? moment(res.body.updateDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((innkeeper: IInnkeeper) => {
        innkeeper.createDate = innkeeper.createDate ? moment(innkeeper.createDate) : undefined;
        innkeeper.updateDate = innkeeper.updateDate ? moment(innkeeper.updateDate) : undefined;
      });
    }
    return res;
  }
}
