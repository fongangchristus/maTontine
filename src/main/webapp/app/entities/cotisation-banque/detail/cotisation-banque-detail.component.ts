import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICotisationBanque } from '../cotisation-banque.model';

@Component({
  selector: 'jhi-cotisation-banque-detail',
  templateUrl: './cotisation-banque-detail.component.html',
})
export class CotisationBanqueDetailComponent implements OnInit {
  cotisationBanque: ICotisationBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisationBanque }) => {
      this.cotisationBanque = cotisationBanque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
