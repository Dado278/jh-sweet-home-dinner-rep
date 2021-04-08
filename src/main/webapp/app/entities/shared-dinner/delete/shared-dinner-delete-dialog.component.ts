import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISharedDinner } from '../shared-dinner.model';
import { SharedDinnerService } from '../service/shared-dinner.service';

@Component({
  templateUrl: './shared-dinner-delete-dialog.component.html',
})
export class SharedDinnerDeleteDialogComponent {
  sharedDinner?: ISharedDinner;

  constructor(protected sharedDinnerService: SharedDinnerService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sharedDinnerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
