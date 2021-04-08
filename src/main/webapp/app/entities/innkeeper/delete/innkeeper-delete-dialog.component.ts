import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInnkeeper } from '../innkeeper.model';
import { InnkeeperService } from '../service/innkeeper.service';

@Component({
  templateUrl: './innkeeper-delete-dialog.component.html',
})
export class InnkeeperDeleteDialogComponent {
  innkeeper?: IInnkeeper;

  constructor(protected innkeeperService: InnkeeperService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.innkeeperService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
