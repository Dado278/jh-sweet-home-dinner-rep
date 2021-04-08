import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SharedDinnerComponent } from './list/shared-dinner.component';
import { SharedDinnerDetailComponent } from './detail/shared-dinner-detail.component';
import { SharedDinnerUpdateComponent } from './update/shared-dinner-update.component';
import { SharedDinnerDeleteDialogComponent } from './delete/shared-dinner-delete-dialog.component';
import { SharedDinnerRoutingModule } from './route/shared-dinner-routing.module';

@NgModule({
  imports: [SharedModule, SharedDinnerRoutingModule],
  declarations: [SharedDinnerComponent, SharedDinnerDetailComponent, SharedDinnerUpdateComponent, SharedDinnerDeleteDialogComponent],
  entryComponents: [SharedDinnerDeleteDialogComponent],
})
export class SharedDinnerModule {}
