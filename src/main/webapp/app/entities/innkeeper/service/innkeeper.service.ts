import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInnkeeper, getInnkeeperIdentifier } from '../innkeeper.model';

export type EntityResponseType = HttpResponse<IInnkeeper>;
export type EntityArrayResponseType = HttpResponse<IInnkeeper[]>;

@Injectable({ providedIn: 'root' })
export class InnkeeperService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/innkeepers');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/innkeepers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(innkeeper: IInnkeeper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(innkeeper);
    return this.http
      .post<IInnkeeper>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(innkeeper: IInnkeeper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(innkeeper);
    return this.http
      .put<IInnkeeper>(`${this.resourceUrl}/${getInnkeeperIdentifier(innkeeper) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(innkeeper: IInnkeeper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(innkeeper);
    return this.http
      .patch<IInnkeeper>(`${this.resourceUrl}/${getInnkeeperIdentifier(innkeeper) as number}`, copy, { observe: 'response' })
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

  addInnkeeperToCollectionIfMissing(
    innkeeperCollection: IInnkeeper[],
    ...innkeepersToCheck: (IInnkeeper | null | undefined)[]
  ): IInnkeeper[] {
    const innkeepers: IInnkeeper[] = innkeepersToCheck.filter(isPresent);
    if (innkeepers.length > 0) {
      const innkeeperCollectionIdentifiers = innkeeperCollection.map(innkeeperItem => getInnkeeperIdentifier(innkeeperItem)!);
      const innkeepersToAdd = innkeepers.filter(innkeeperItem => {
        const innkeeperIdentifier = getInnkeeperIdentifier(innkeeperItem);
        if (innkeeperIdentifier == null || innkeeperCollectionIdentifiers.includes(innkeeperIdentifier)) {
          return false;
        }
        innkeeperCollectionIdentifiers.push(innkeeperIdentifier);
        return true;
      });
      return [...innkeepersToAdd, ...innkeeperCollection];
    }
    return innkeeperCollection;
  }

  protected convertDateFromClient(innkeeper: IInnkeeper): IInnkeeper {
    return Object.assign({}, innkeeper, {
      createDate: innkeeper.createDate?.isValid() ? innkeeper.createDate.toJSON() : undefined,
      updateDate: innkeeper.updateDate?.isValid() ? innkeeper.updateDate.toJSON() : undefined,
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
      res.body.forEach((innkeeper: IInnkeeper) => {
        innkeeper.createDate = innkeeper.createDate ? dayjs(innkeeper.createDate) : undefined;
        innkeeper.updateDate = innkeeper.updateDate ? dayjs(innkeeper.updateDate) : undefined;
      });
    }
    return res;
  }
}
