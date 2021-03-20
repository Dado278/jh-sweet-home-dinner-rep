import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'people',
        loadChildren: () => import('./people/people.module').then(m => m.JhSweetHomeDinnerApplicationPeopleModule),
      },
      {
        path: 'info-innkeeper',
        loadChildren: () => import('./info-innkeeper/info-innkeeper.module').then(m => m.JhSweetHomeDinnerApplicationInfoInnkeeperModule),
      },
      {
        path: 'shared-dinner',
        loadChildren: () => import('./shared-dinner/shared-dinner.module').then(m => m.JhSweetHomeDinnerApplicationSharedDinnerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JhSweetHomeDinnerApplicationEntityModule {}
