import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { TakeAwayComponent } from './take-away.component';
import { TakeAwayDetailComponent } from './take-away-detail.component';
import { TakeAwayUpdateComponent } from './take-away-update.component';
import { TakeAwayDeleteDialogComponent } from './take-away-delete-dialog.component';
import { takeAwayRoute } from './take-away.route';

@NgModule({
  imports: [JhSweetHomeDinnerApplicationSharedModule, RouterModule.forChild(takeAwayRoute)],
  declarations: [TakeAwayComponent, TakeAwayDetailComponent, TakeAwayUpdateComponent, TakeAwayDeleteDialogComponent],
  entryComponents: [TakeAwayDeleteDialogComponent],
})
export class JhSweetHomeDinnerApplicationTakeAwayModule {}
