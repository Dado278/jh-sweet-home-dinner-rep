import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TakeAwayComponent } from './list/take-away.component';
import { TakeAwayDetailComponent } from './detail/take-away-detail.component';
import { TakeAwayUpdateComponent } from './update/take-away-update.component';
import { TakeAwayDeleteDialogComponent } from './delete/take-away-delete-dialog.component';
import { TakeAwayRoutingModule } from './route/take-away-routing.module';

@NgModule({
  imports: [SharedModule, TakeAwayRoutingModule],
  declarations: [TakeAwayComponent, TakeAwayDetailComponent, TakeAwayUpdateComponent, TakeAwayDeleteDialogComponent],
  entryComponents: [TakeAwayDeleteDialogComponent],
})
export class TakeAwayModule {}
