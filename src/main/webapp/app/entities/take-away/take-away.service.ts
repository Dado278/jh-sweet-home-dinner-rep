import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ITakeAway } from 'app/shared/model/take-away.model';

type EntityResponseType = HttpResponse<ITakeAway>;
type EntityArrayResponseType = HttpResponse<ITakeAway[]>;

@Injectable({ providedIn: 'root' })
export class TakeAwayService {
  public resourceUrl = SERVER_API_URL + 'api/take-aways';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/take-aways';

  constructor(protected http: HttpClient) {}

  create(takeAway: ITakeAway): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(takeAway);
    return this.http
      .post<ITakeAway>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(takeAway: ITakeAway): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(takeAway);
    return this.http
      .put<ITakeAway>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITakeAway>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITakeAway[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITakeAway[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(takeAway: ITakeAway): ITakeAway {
    const copy: ITakeAway = Object.assign({}, takeAway, {
      createDate: takeAway.createDate && takeAway.createDate.isValid() ? takeAway.createDate.toJSON() : undefined,
      updateDate: takeAway.updateDate && takeAway.updateDate.isValid() ? takeAway.updateDate.toJSON() : undefined,
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
      res.body.forEach((takeAway: ITakeAway) => {
        takeAway.createDate = takeAway.createDate ? moment(takeAway.createDate) : undefined;
        takeAway.updateDate = takeAway.updateDate ? moment(takeAway.updateDate) : undefined;
      });
    }
    return res;
  }
}
