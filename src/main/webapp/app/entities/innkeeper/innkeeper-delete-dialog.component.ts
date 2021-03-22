import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInnkeeper } from 'app/shared/model/innkeeper.model';
import { InnkeeperService } from './innkeeper.service';

@Component({
  templateUrl: './innkeeper-delete-dialog.component.html',
})
export class InnkeeperDeleteDialogComponent {
  innkeeper?: IInnkeeper;

  constructor(protected innkeeperService: InnkeeperService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.innkeeperService.delete(id).subscribe(() => {
      this.eventManager.broadcast('innkeeperListModification');
      this.activeModal.close();
    });
  }
}
