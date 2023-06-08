import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICotisationTontine } from '../cotisation-tontine.model';

@Component({
  selector: 'jhi-cotisation-tontine-detail',
  templateUrl: './cotisation-tontine-detail.component.html',
})
export class CotisationTontineDetailComponent implements OnInit {
  cotisationTontine: ICotisationTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisationTontine }) => {
      this.cotisationTontine = cotisationTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
