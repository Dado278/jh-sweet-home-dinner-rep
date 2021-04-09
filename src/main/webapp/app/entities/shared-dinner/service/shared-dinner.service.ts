import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISharedDinner, getSharedDinnerIdentifier } from '../shared-dinner.model';

export type EntityResponseType = HttpResponse<ISharedDinner>;
export type EntityArrayResponseType = HttpResponse<ISharedDinner[]>;

@Injectable({ providedIn: 'root' })
export class SharedDinnerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/shared-dinners');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/shared-dinners');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sharedDinner: ISharedDinner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sharedDinner);
    return this.http
      .post<ISharedDinner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sharedDinner: ISharedDinner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sharedDinner);
    return this.http
      .put<ISharedDinner>(`${this.resourceUrl}/${getSharedDinnerIdentifier(sharedDinner) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sharedDinner: ISharedDinner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sharedDinner);
    return this.http
      .patch<ISharedDinner>(`${this.resourceUrl}/${getSharedDinnerIdentifier(sharedDinner) as number}`, copy, { observe: 'response' })
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

  addSharedDinnerToCollectionIfMissing(
    sharedDinnerCollection: ISharedDinner[],
    ...sharedDinnersToCheck: (ISharedDinner | null | undefined)[]
  ): ISharedDinner[] {
    const sharedDinners: ISharedDinner[] = sharedDinnersToCheck.filter(isPresent);
    if (sharedDinners.length > 0) {
      const sharedDinnerCollectionIdentifiers = sharedDinnerCollection.map(
        sharedDinnerItem => getSharedDinnerIdentifier(sharedDinnerItem)!
      );
      const sharedDinnersToAdd = sharedDinners.filter(sharedDinnerItem => {
        const sharedDinnerIdentifier = getSharedDinnerIdentifier(sharedDinnerItem);
        if (sharedDinnerIdentifier == null || sharedDinnerCollectionIdentifiers.includes(sharedDinnerIdentifier)) {
          return false;
        }
        sharedDinnerCollectionIdentifiers.push(sharedDinnerIdentifier);
        return true;
      });
      return [...sharedDinnersToAdd, ...sharedDinnerCollection];
    }
    return sharedDinnerCollection;
  }

  protected convertDateFromClient(sharedDinner: ISharedDinner): ISharedDinner {
    return Object.assign({}, sharedDinner, {
      createDate: sharedDinner.createDate?.isValid() ? sharedDinner.createDate.toJSON() : undefined,
      updateDate: sharedDinner.updateDate?.isValid() ? sharedDinner.updateDate.toJSON() : undefined,
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
      res.body.forEach((sharedDinner: ISharedDinner) => {
        sharedDinner.createDate = sharedDinner.createDate ? dayjs(sharedDinner.createDate) : undefined;
        sharedDinner.updateDate = sharedDinner.updateDate ? dayjs(sharedDinner.updateDate) : undefined;
      });
    }
    return res;
  }
}
