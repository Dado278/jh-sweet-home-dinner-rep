import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITakeAway, TakeAway } from '../take-away.model';
import { TakeAwayService } from '../service/take-away.service';

@Injectable({ providedIn: 'root' })
export class TakeAwayRoutingResolveService implements Resolve<ITakeAway> {
  constructor(protected service: TakeAwayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITakeAway> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((takeAway: HttpResponse<TakeAway>) => {
          if (takeAway.body) {
            return of(takeAway.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TakeAway());
  }
}
