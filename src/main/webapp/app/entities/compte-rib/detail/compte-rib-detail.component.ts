import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompteRIB } from '../compte-rib.model';

@Component({
  selector: 'jhi-compte-rib-detail',
  templateUrl: './compte-rib-detail.component.html',
})
export class CompteRIBDetailComponent implements OnInit {
  compteRIB: ICompteRIB | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteRIB }) => {
      this.compteRIB = compteRIB;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
