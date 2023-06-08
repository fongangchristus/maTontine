import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDecaisementBanque } from '../decaisement-banque.model';

@Component({
  selector: 'jhi-decaisement-banque-detail',
  templateUrl: './decaisement-banque-detail.component.html',
})
export class DecaisementBanqueDetailComponent implements OnInit {
  decaisementBanque: IDecaisementBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decaisementBanque }) => {
      this.decaisementBanque = decaisementBanque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
