import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompteTontine } from '../compte-tontine.model';

@Component({
  selector: 'jhi-compte-tontine-detail',
  templateUrl: './compte-tontine-detail.component.html',
})
export class CompteTontineDetailComponent implements OnInit {
  compteTontine: ICompteTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteTontine }) => {
      this.compteTontine = compteTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
