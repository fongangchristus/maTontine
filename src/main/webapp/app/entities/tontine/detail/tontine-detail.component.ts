import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITontine } from '../tontine.model';

@Component({
  selector: 'jhi-tontine-detail',
  templateUrl: './tontine-detail.component.html',
})
export class TontineDetailComponent implements OnInit {
  tontine: ITontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tontine }) => {
      this.tontine = tontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
