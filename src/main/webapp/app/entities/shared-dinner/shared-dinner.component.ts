import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISharedDinner } from 'app/shared/model/shared-dinner.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SharedDinnerService } from './shared-dinner.service';
import { SharedDinnerDeleteDialogComponent } from './shared-dinner-delete-dialog.component';

@Component({
  selector: 'jhi-shared-dinner',
  templateUrl: './shared-dinner.component.html',
})
export class SharedDinnerComponent implements OnInit, OnDestroy {
  sharedDinners: ISharedDinner[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected sharedDinnerService: SharedDinnerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.sharedDinners = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.sharedDinnerService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<ISharedDinner[]>) => this.paginateSharedDinners(res.body, res.headers));
      return;
    }

    this.sharedDinnerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ISharedDinner[]>) => this.paginateSharedDinners(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.sharedDinners = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.sharedDinners = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSharedDinners();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISharedDinner): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSharedDinners(): void {
    this.eventSubscriber = this.eventManager.subscribe('sharedDinnerListModification', () => this.reset());
  }

  delete(sharedDinner: ISharedDinner): void {
    const modalRef = this.modalService.open(SharedDinnerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sharedDinner = sharedDinner;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSharedDinners(data: ISharedDinner[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.sharedDinners.push(data[i]);
      }
    }
  }
}
