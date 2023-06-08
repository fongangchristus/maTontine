import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDecaissementTontine } from '../decaissement-tontine.model';

@Component({
  selector: 'jhi-decaissement-tontine-detail',
  templateUrl: './decaissement-tontine-detail.component.html',
})
export class DecaissementTontineDetailComponent implements OnInit {
  decaissementTontine: IDecaissementTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decaissementTontine }) => {
      this.decaissementTontine = decaissementTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
