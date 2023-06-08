import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPot } from '../pot.model';

@Component({
  selector: 'jhi-pot-detail',
  templateUrl: './pot-detail.component.html',
})
export class PotDetailComponent implements OnInit {
  pot: IPot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pot }) => {
      this.pot = pot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
