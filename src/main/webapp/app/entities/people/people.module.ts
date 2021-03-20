import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { PeopleComponent } from './people.component';
import { PeopleDetailComponent } from './people-detail.component';
import { PeopleUpdateComponent } from './people-update.component';
import { PeopleDeleteDialogComponent } from './people-delete-dialog.component';
import { peopleRoute } from './people.route';

@NgModule({
  imports: [JhSweetHomeDinnerApplicationSharedModule, RouterModule.forChild(peopleRoute)],
  declarations: [PeopleComponent, PeopleDetailComponent, PeopleUpdateComponent, PeopleDeleteDialogComponent],
  entryComponents: [PeopleDeleteDialogComponent],
})
export class JhSweetHomeDinnerApplicationPeopleModule {}
