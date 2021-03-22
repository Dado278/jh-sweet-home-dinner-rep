import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInnkeeper } from 'app/shared/model/innkeeper.model';

@Component({
  selector: 'jhi-innkeeper-detail',
  templateUrl: './innkeeper-detail.component.html',
})
export class InnkeeperDetailComponent implements OnInit {
  innkeeper: IInnkeeper | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ innkeeper }) => (this.innkeeper = innkeeper));
  }

  previousState(): void {
    window.history.back();
  }
}
