import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITakeAway, getTakeAwayIdentifier } from '../take-away.model';

export type EntityResponseType = HttpResponse<ITakeAway>;
export type EntityArrayResponseType = HttpResponse<ITakeAway[]>;

@Injectable({ providedIn: 'root' })
export class TakeAwayService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/take-aways');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/take-aways');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(takeAway: ITakeAway): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(takeAway);
    return this.http
      .post<ITakeAway>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(takeAway: ITakeAway): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(takeAway);
    return this.http
      .put<ITakeAway>(`${this.resourceUrl}/${getTakeAwayIdentifier(takeAway) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(takeAway: ITakeAway): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(takeAway);
    return this.http
      .patch<ITakeAway>(`${this.resourceUrl}/${getTakeAwayIdentifier(takeAway) as number}`, copy, { observe: 'response' })
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

  addTakeAwayToCollectionIfMissing(takeAwayCollection: ITakeAway[], ...takeAwaysToCheck: (ITakeAway | null | undefined)[]): ITakeAway[] {
    const takeAways: ITakeAway[] = takeAwaysToCheck.filter(isPresent);
    if (takeAways.length > 0) {
      const takeAwayCollectionIdentifiers = takeAwayCollection.map(takeAwayItem => getTakeAwayIdentifier(takeAwayItem)!);
      const takeAwaysToAdd = takeAways.filter(takeAwayItem => {
        const takeAwayIdentifier = getTakeAwayIdentifier(takeAwayItem);
        if (takeAwayIdentifier == null || takeAwayCollectionIdentifiers.includes(takeAwayIdentifier)) {
          return false;
        }
        takeAwayCollectionIdentifiers.push(takeAwayIdentifier);
        return true;
      });
      return [...takeAwaysToAdd, ...takeAwayCollection];
    }
    return takeAwayCollection;
  }

  protected convertDateFromClient(takeAway: ITakeAway): ITakeAway {
    return Object.assign({}, takeAway, {
      createDate: takeAway.createDate?.isValid() ? takeAway.createDate.toJSON() : undefined,
      updateDate: takeAway.updateDate?.isValid() ? takeAway.updateDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.updateDate = res.body.updateDate ? dayjs(res.body.updateDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((takeAway: ITakeAway) => {
        takeAway.createDate = takeAway.createDate ? dayjs(takeAway.createDate) : undefined;
        takeAway.updateDate = takeAway.updateDate ? dayjs(takeAway.updateDate) : undefined;
      });
    }
    return res;
  }
}
