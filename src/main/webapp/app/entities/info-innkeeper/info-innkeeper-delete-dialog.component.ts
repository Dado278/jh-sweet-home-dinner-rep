import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { InfoInnkeeperService } from './info-innkeeper.service';

@Component({
  templateUrl: './info-innkeeper-delete-dialog.component.html',
})
export class InfoInnkeeperDeleteDialogComponent {
  infoInnkeeper?: IInfoInnkeeper;

  constructor(
    protected infoInnkeeperService: InfoInnkeeperService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infoInnkeeperService.delete(id).subscribe(() => {
      this.eventManager.broadcast('infoInnkeeperListModification');
      this.activeModal.close();
    });
  }
}
