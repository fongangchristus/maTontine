import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaiementSanction } from '../paiement-sanction.model';

@Component({
  selector: 'jhi-paiement-sanction-detail',
  templateUrl: './paiement-sanction-detail.component.html',
})
export class PaiementSanctionDetailComponent implements OnInit {
  paiementSanction: IPaiementSanction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementSanction }) => {
      this.paiementSanction = paiementSanction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
