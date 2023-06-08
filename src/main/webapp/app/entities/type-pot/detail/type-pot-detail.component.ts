import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePot } from '../type-pot.model';

@Component({
  selector: 'jhi-type-pot-detail',
  templateUrl: './type-pot-detail.component.html',
})
export class TypePotDetailComponent implements OnInit {
  typePot: ITypePot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePot }) => {
      this.typePot = typePot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
