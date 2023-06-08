import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormuleAdhesion } from '../formule-adhesion.model';

@Component({
  selector: 'jhi-formule-adhesion-detail',
  templateUrl: './formule-adhesion-detail.component.html',
})
export class FormuleAdhesionDetailComponent implements OnInit {
  formuleAdhesion: IFormuleAdhesion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formuleAdhesion }) => {
      this.formuleAdhesion = formuleAdhesion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
