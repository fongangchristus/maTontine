import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaiementTontine } from '../paiement-tontine.model';

@Component({
  selector: 'jhi-paiement-tontine-detail',
  templateUrl: './paiement-tontine-detail.component.html',
})
export class PaiementTontineDetailComponent implements OnInit {
  paiementTontine: IPaiementTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementTontine }) => {
      this.paiementTontine = paiementTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
