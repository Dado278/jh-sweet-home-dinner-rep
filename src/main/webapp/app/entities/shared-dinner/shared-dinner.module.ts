import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { SharedDinnerComponent } from './shared-dinner.component';
import { SharedDinnerDetailComponent } from './shared-dinner-detail.component';
import { SharedDinnerUpdateComponent } from './shared-dinner-update.component';
import { SharedDinnerDeleteDialogComponent } from './shared-dinner-delete-dialog.component';
import { sharedDinnerRoute } from './shared-dinner.route';

@NgModule({
  imports: [JhSweetHomeDinnerApplicationSharedModule, RouterModule.forChild(sharedDinnerRoute)],
  declarations: [SharedDinnerComponent, SharedDinnerDetailComponent, SharedDinnerUpdateComponent, SharedDinnerDeleteDialogComponent],
  entryComponents: [SharedDinnerDeleteDialogComponent],
})
export class JhSweetHomeDinnerApplicationSharedDinnerModule {}
