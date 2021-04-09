import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInnkeeper, Innkeeper } from '../innkeeper.model';
import { InnkeeperService } from '../service/innkeeper.service';

@Injectable({ providedIn: 'root' })
export class InnkeeperRoutingResolveService implements Resolve<IInnkeeper> {
  constructor(protected service: InnkeeperService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInnkeeper> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((innkeeper: HttpResponse<Innkeeper>) => {
          if (innkeeper.body) {
            return of(innkeeper.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Innkeeper());
  }
}
