import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITakeAway } from 'app/shared/model/take-away.model';

@Component({
  selector: 'jhi-take-away-detail',
  templateUrl: './take-away-detail.component.html',
})
export class TakeAwayDetailComponent implements OnInit {
  takeAway: ITakeAway | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takeAway }) => (this.takeAway = takeAway));
  }

  previousState(): void {
    window.history.back();
  }
}
