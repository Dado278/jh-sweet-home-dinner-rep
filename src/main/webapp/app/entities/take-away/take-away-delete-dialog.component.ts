import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITakeAway } from 'app/shared/model/take-away.model';
import { TakeAwayService } from './take-away.service';

@Component({
  templateUrl: './take-away-delete-dialog.component.html',
})
export class TakeAwayDeleteDialogComponent {
  takeAway?: ITakeAway;

  constructor(protected takeAwayService: TakeAwayService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.takeAwayService.delete(id).subscribe(() => {
      this.eventManager.broadcast('takeAwayListModification');
      this.activeModal.close();
    });
  }
}
