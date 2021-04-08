import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InnkeeperComponent } from '../list/innkeeper.component';
import { InnkeeperDetailComponent } from '../detail/innkeeper-detail.component';
import { InnkeeperUpdateComponent } from '../update/innkeeper-update.component';
import { InnkeeperRoutingResolveService } from './innkeeper-routing-resolve.service';

const innkeeperRoute: Routes = [
  {
    path: '',
    component: InnkeeperComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InnkeeperDetailComponent,
    resolve: {
      innkeeper: InnkeeperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InnkeeperUpdateComponent,
    resolve: {
      innkeeper: InnkeeperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InnkeeperUpdateComponent,
    resolve: {
      innkeeper: InnkeeperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(innkeeperRoute)],
  exports: [RouterModule],
})
export class InnkeeperRoutingModule {}
