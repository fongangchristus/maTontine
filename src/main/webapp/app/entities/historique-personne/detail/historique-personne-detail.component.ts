import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHistoriquePersonne } from '../historique-personne.model';

@Component({
  selector: 'jhi-historique-personne-detail',
  templateUrl: './historique-personne-detail.component.html',
})
export class HistoriquePersonneDetailComponent implements OnInit {
  historiquePersonne: IHistoriquePersonne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiquePersonne }) => {
      this.historiquePersonne = historiquePersonne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
