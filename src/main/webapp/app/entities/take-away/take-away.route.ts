import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITakeAway, TakeAway } from 'app/shared/model/take-away.model';
import { TakeAwayService } from './take-away.service';
import { TakeAwayComponent } from './take-away.component';
import { TakeAwayDetailComponent } from './take-away-detail.component';
import { TakeAwayUpdateComponent } from './take-away-update.component';

@Injectable({ providedIn: 'root' })
export class TakeAwayResolve implements Resolve<ITakeAway> {
  constructor(private service: TakeAwayService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITakeAway> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((takeAway: HttpResponse<TakeAway>) => {
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

export const takeAwayRoute: Routes = [
  {
    path: '',
    component: TakeAwayComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'jhSweetHomeDinnerApplicationApp.takeAway.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TakeAwayDetailComponent,
    resolve: {
      takeAway: TakeAwayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.takeAway.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TakeAwayUpdateComponent,
    resolve: {
      takeAway: TakeAwayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.takeAway.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TakeAwayUpdateComponent,
    resolve: {
      takeAway: TakeAwayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.takeAway.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
