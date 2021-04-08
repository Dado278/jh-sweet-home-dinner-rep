import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SharedDinnerComponent } from '../list/shared-dinner.component';
import { SharedDinnerDetailComponent } from '../detail/shared-dinner-detail.component';
import { SharedDinnerUpdateComponent } from '../update/shared-dinner-update.component';
import { SharedDinnerRoutingResolveService } from './shared-dinner-routing-resolve.service';

const sharedDinnerRoute: Routes = [
  {
    path: '',
    component: SharedDinnerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SharedDinnerDetailComponent,
    resolve: {
      sharedDinner: SharedDinnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SharedDinnerUpdateComponent,
    resolve: {
      sharedDinner: SharedDinnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SharedDinnerUpdateComponent,
    resolve: {
      sharedDinner: SharedDinnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sharedDinnerRoute)],
  exports: [RouterModule],
})
export class SharedDinnerRoutingModule {}
