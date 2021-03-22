import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.JhSweetHomeDinnerApplicationCustomerModule),
      },
      {
        path: 'innkeeper',
        loadChildren: () => import('./innkeeper/innkeeper.module').then(m => m.JhSweetHomeDinnerApplicationInnkeeperModule),
      },
      {
        path: 'shared-dinner',
        loadChildren: () => import('./shared-dinner/shared-dinner.module').then(m => m.JhSweetHomeDinnerApplicationSharedDinnerModule),
      },
      {
        path: 'take-away',
        loadChildren: () => import('./take-away/take-away.module').then(m => m.JhSweetHomeDinnerApplicationTakeAwayModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JhSweetHomeDinnerApplicationEntityModule {}
