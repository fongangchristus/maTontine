import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaiementBanque } from '../paiement-banque.model';

@Component({
  selector: 'jhi-paiement-banque-detail',
  templateUrl: './paiement-banque-detail.component.html',
})
export class PaiementBanqueDetailComponent implements OnInit {
  paiementBanque: IPaiementBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementBanque }) => {
      this.paiementBanque = paiementBanque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
