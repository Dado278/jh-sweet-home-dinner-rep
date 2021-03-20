import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeople } from 'app/shared/model/people.model';

@Component({
  selector: 'jhi-people-detail',
  templateUrl: './people-detail.component.html',
})
export class PeopleDetailComponent implements OnInit {
  people: IPeople | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ people }) => (this.people = people));
  }

  previousState(): void {
    window.history.back();
  }
}
