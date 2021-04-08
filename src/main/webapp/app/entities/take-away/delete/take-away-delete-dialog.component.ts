import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITakeAway } from '../take-away.model';
import { TakeAwayService } from '../service/take-away.service';

@Component({
  templateUrl: './take-away-delete-dialog.component.html',
})
export class TakeAwayDeleteDialogComponent {
  takeAway?: ITakeAway;

  constructor(protected takeAwayService: TakeAwayService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.takeAwayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
