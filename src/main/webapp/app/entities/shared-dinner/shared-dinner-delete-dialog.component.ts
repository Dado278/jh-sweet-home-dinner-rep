import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISharedDinner } from 'app/shared/model/shared-dinner.model';
import { SharedDinnerService } from './shared-dinner.service';

@Component({
  templateUrl: './shared-dinner-delete-dialog.component.html',
})
export class SharedDinnerDeleteDialogComponent {
  sharedDinner?: ISharedDinner;

  constructor(
    protected sharedDinnerService: SharedDinnerService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sharedDinnerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sharedDinnerListModification');
      this.activeModal.close();
    });
  }
}
