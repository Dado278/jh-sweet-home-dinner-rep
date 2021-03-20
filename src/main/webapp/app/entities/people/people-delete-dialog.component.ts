import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeople } from 'app/shared/model/people.model';
import { PeopleService } from './people.service';

@Component({
  templateUrl: './people-delete-dialog.component.html',
})
export class PeopleDeleteDialogComponent {
  people?: IPeople;

  constructor(protected peopleService: PeopleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peopleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('peopleListModification');
      this.activeModal.close();
    });
  }
}
