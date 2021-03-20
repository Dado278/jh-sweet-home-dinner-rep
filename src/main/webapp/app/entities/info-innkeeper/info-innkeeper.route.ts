import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInfoInnkeeper, InfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { InfoInnkeeperService } from './info-innkeeper.service';
import { InfoInnkeeperComponent } from './info-innkeeper.component';
import { InfoInnkeeperDetailComponent } from './info-innkeeper-detail.component';
import { InfoInnkeeperUpdateComponent } from './info-innkeeper-update.component';

@Injectable({ providedIn: 'root' })
export class InfoInnkeeperResolve implements Resolve<IInfoInnkeeper> {
  constructor(private service: InfoInnkeeperService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfoInnkeeper> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((infoInnkeeper: HttpResponse<InfoInnkeeper>) => {
          if (infoInnkeeper.body) {
            return of(infoInnkeeper.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfoInnkeeper());
  }
}

export const infoInnkeeperRoute: Routes = [
  {
    path: '',
    component: InfoInnkeeperComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'jhSweetHomeDinnerApplicationApp.infoInnkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfoInnkeeperDetailComponent,
    resolve: {
      infoInnkeeper: InfoInnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.infoInnkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfoInnkeeperUpdateComponent,
    resolve: {
      infoInnkeeper: InfoInnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.infoInnkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfoInnkeeperUpdateComponent,
    resolve: {
      infoInnkeeper: InfoInnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.infoInnkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
