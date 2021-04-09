import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TakeAwayComponent } from '../list/take-away.component';
import { TakeAwayDetailComponent } from '../detail/take-away-detail.component';
import { TakeAwayUpdateComponent } from '../update/take-away-update.component';
import { TakeAwayRoutingResolveService } from './take-away-routing-resolve.service';

const takeAwayRoute: Routes = [
  {
    path: '',
    component: TakeAwayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TakeAwayDetailComponent,
    resolve: {
      takeAway: TakeAwayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TakeAwayUpdateComponent,
    resolve: {
      takeAway: TakeAwayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TakeAwayUpdateComponent,
    resolve: {
      takeAway: TakeAwayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(takeAwayRoute)],
  exports: [RouterModule],
})
export class TakeAwayRoutingModule {}
