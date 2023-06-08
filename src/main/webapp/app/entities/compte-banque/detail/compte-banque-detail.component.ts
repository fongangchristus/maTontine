import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompteBanque } from '../compte-banque.model';

@Component({
  selector: 'jhi-compte-banque-detail',
  templateUrl: './compte-banque-detail.component.html',
})
export class CompteBanqueDetailComponent implements OnInit {
  compteBanque: ICompteBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteBanque }) => {
      this.compteBanque = compteBanque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
