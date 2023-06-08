import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGestionnaireTontine } from '../gestionnaire-tontine.model';

@Component({
  selector: 'jhi-gestionnaire-tontine-detail',
  templateUrl: './gestionnaire-tontine-detail.component.html',
})
export class GestionnaireTontineDetailComponent implements OnInit {
  gestionnaireTontine: IGestionnaireTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestionnaireTontine }) => {
      this.gestionnaireTontine = gestionnaireTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
