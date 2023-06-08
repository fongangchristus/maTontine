import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMonnaie } from '../monnaie.model';

@Component({
  selector: 'jhi-monnaie-detail',
  templateUrl: './monnaie-detail.component.html',
})
export class MonnaieDetailComponent implements OnInit {
  monnaie: IMonnaie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monnaie }) => {
      this.monnaie = monnaie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
