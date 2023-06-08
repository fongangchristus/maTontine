import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISanction } from '../sanction.model';

@Component({
  selector: 'jhi-sanction-detail',
  templateUrl: './sanction-detail.component.html',
})
export class SanctionDetailComponent implements OnInit {
  sanction: ISanction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanction }) => {
      this.sanction = sanction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
