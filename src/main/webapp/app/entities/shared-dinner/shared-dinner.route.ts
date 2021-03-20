import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISharedDinner, SharedDinner } from 'app/shared/model/shared-dinner.model';
import { SharedDinnerService } from './shared-dinner.service';
import { SharedDinnerComponent } from './shared-dinner.component';
import { SharedDinnerDetailComponent } from './shared-dinner-detail.component';
import { SharedDinnerUpdateComponent } from './shared-dinner-update.component';

@Injectable({ providedIn: 'root' })
export class SharedDinnerResolve implements Resolve<ISharedDinner> {
  constructor(private service: SharedDinnerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISharedDinner> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sharedDinner: HttpResponse<SharedDinner>) => {
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

export const sharedDinnerRoute: Routes = [
  {
    path: '',
    component: SharedDinnerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.sharedDinner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SharedDinnerDetailComponent,
    resolve: {
      sharedDinner: SharedDinnerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.sharedDinner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SharedDinnerUpdateComponent,
    resolve: {
      sharedDinner: SharedDinnerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.sharedDinner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SharedDinnerUpdateComponent,
    resolve: {
      sharedDinner: SharedDinnerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.sharedDinner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
