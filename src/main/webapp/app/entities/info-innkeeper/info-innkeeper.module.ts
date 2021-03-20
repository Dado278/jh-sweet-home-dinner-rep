import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { InfoInnkeeperComponent } from './info-innkeeper.component';
import { InfoInnkeeperDetailComponent } from './info-innkeeper-detail.component';
import { InfoInnkeeperUpdateComponent } from './info-innkeeper-update.component';
import { InfoInnkeeperDeleteDialogComponent } from './info-innkeeper-delete-dialog.component';
import { infoInnkeeperRoute } from './info-innkeeper.route';

@NgModule({
  imports: [JhSweetHomeDinnerApplicationSharedModule, RouterModule.forChild(infoInnkeeperRoute)],
  declarations: [InfoInnkeeperComponent, InfoInnkeeperDetailComponent, InfoInnkeeperUpdateComponent, InfoInnkeeperDeleteDialogComponent],
  entryComponents: [InfoInnkeeperDeleteDialogComponent],
})
export class JhSweetHomeDinnerApplicationInfoInnkeeperModule {}
