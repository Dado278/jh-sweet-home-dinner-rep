import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInnkeeper, Innkeeper } from 'app/shared/model/innkeeper.model';
import { InnkeeperService } from './innkeeper.service';
import { InnkeeperComponent } from './innkeeper.component';
import { InnkeeperDetailComponent } from './innkeeper-detail.component';
import { InnkeeperUpdateComponent } from './innkeeper-update.component';

@Injectable({ providedIn: 'root' })
export class InnkeeperResolve implements Resolve<IInnkeeper> {
  constructor(private service: InnkeeperService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInnkeeper> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((innkeeper: HttpResponse<Innkeeper>) => {
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

export const innkeeperRoute: Routes = [
  {
    path: '',
    component: InnkeeperComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.innkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InnkeeperDetailComponent,
    resolve: {
      innkeeper: InnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.innkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InnkeeperUpdateComponent,
    resolve: {
      innkeeper: InnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.innkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InnkeeperUpdateComponent,
    resolve: {
      innkeeper: InnkeeperResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhSweetHomeDinnerApplicationApp.innkeeper.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
