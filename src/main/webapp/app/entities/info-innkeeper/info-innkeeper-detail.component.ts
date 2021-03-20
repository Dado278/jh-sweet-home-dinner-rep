import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

@Component({
  selector: 'jhi-info-innkeeper-detail',
  templateUrl: './info-innkeeper-detail.component.html',
})
export class InfoInnkeeperDetailComponent implements OnInit {
  infoInnkeeper: IInfoInnkeeper | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infoInnkeeper }) => (this.infoInnkeeper = infoInnkeeper));
  }

  previousState(): void {
    window.history.back();
  }
}
