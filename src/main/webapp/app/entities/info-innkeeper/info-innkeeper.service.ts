import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

type EntityResponseType = HttpResponse<IInfoInnkeeper>;
type EntityArrayResponseType = HttpResponse<IInfoInnkeeper[]>;

@Injectable({ providedIn: 'root' })
export class InfoInnkeeperService {
  public resourceUrl = SERVER_API_URL + 'api/info-innkeepers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/info-innkeepers';

  constructor(protected http: HttpClient) {}

  create(infoInnkeeper: IInfoInnkeeper): Observable<EntityResponseType> {
    return this.http.post<IInfoInnkeeper>(this.resourceUrl, infoInnkeeper, { observe: 'response' });
  }

  update(infoInnkeeper: IInfoInnkeeper): Observable<EntityResponseType> {
    return this.http.put<IInfoInnkeeper>(this.resourceUrl, infoInnkeeper, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfoInnkeeper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfoInnkeeper[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfoInnkeeper[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
