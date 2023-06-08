import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGestionnaireBanque } from '../gestionnaire-banque.model';

@Component({
  selector: 'jhi-gestionnaire-banque-detail',
  templateUrl: './gestionnaire-banque-detail.component.html',
})
export class GestionnaireBanqueDetailComponent implements OnInit {
  gestionnaireBanque: IGestionnaireBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestionnaireBanque }) => {
      this.gestionnaireBanque = gestionnaireBanque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
