import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssemble } from '../assemble.model';

@Component({
  selector: 'jhi-assemble-detail',
  templateUrl: './assemble-detail.component.html',
})
export class AssembleDetailComponent implements OnInit {
  assemble: IAssemble | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assemble }) => {
      this.assemble = assemble;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
