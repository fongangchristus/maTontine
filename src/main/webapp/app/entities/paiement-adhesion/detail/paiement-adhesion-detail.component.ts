import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaiementAdhesion } from '../paiement-adhesion.model';

@Component({
  selector: 'jhi-paiement-adhesion-detail',
  templateUrl: './paiement-adhesion-detail.component.html',
})
export class PaiementAdhesionDetailComponent implements OnInit {
  paiementAdhesion: IPaiementAdhesion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementAdhesion }) => {
      this.paiementAdhesion = paiementAdhesion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
