import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { InnkeeperComponent } from './innkeeper.component';
import { InnkeeperDetailComponent } from './innkeeper-detail.component';
import { InnkeeperUpdateComponent } from './innkeeper-update.component';
import { InnkeeperDeleteDialogComponent } from './innkeeper-delete-dialog.component';
import { innkeeperRoute } from './innkeeper.route';

@NgModule({
  imports: [JhSweetHomeDinnerApplicationSharedModule, RouterModule.forChild(innkeeperRoute)],
  declarations: [InnkeeperComponent, InnkeeperDetailComponent, InnkeeperUpdateComponent, InnkeeperDeleteDialogComponent],
  entryComponents: [InnkeeperDeleteDialogComponent],
})
export class JhSweetHomeDinnerApplicationInnkeeperModule {}
