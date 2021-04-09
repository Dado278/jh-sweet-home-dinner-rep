import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInnkeeper } from '../innkeeper.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-innkeeper-detail',
  templateUrl: './innkeeper-detail.component.html',
})
export class InnkeeperDetailComponent implements OnInit {
  innkeeper: IInnkeeper | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ innkeeper }) => {
      this.innkeeper = innkeeper;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
