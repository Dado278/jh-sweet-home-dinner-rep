import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISharedDinner } from '../shared-dinner.model';

@Component({
  selector: 'jhi-shared-dinner-detail',
  templateUrl: './shared-dinner-detail.component.html',
})
export class SharedDinnerDetailComponent implements OnInit {
  sharedDinner: ISharedDinner | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sharedDinner }) => {
      this.sharedDinner = sharedDinner;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
