import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISharedDinner, SharedDinner } from '../shared-dinner.model';
import { SharedDinnerService } from '../service/shared-dinner.service';

@Injectable({ providedIn: 'root' })
export class SharedDinnerRoutingResolveService implements Resolve<ISharedDinner> {
  constructor(protected service: SharedDinnerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISharedDinner> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sharedDinner: HttpResponse<SharedDinner>) => {
          if (sharedDinner.body) {
            return of(sharedDinner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SharedDinner());
  }
}
