import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InnkeeperComponent } from './list/innkeeper.component';
import { InnkeeperDetailComponent } from './detail/innkeeper-detail.component';
import { InnkeeperUpdateComponent } from './update/innkeeper-update.component';
import { InnkeeperDeleteDialogComponent } from './delete/innkeeper-delete-dialog.component';
import { InnkeeperRoutingModule } from './route/innkeeper-routing.module';

@NgModule({
  imports: [SharedModule, InnkeeperRoutingModule],
  declarations: [InnkeeperComponent, InnkeeperDetailComponent, InnkeeperUpdateComponent, InnkeeperDeleteDialogComponent],
  entryComponents: [InnkeeperDeleteDialogComponent],
})
export class InnkeeperModule {}
