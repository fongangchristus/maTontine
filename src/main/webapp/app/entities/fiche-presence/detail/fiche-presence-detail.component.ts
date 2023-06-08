import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFichePresence } from '../fiche-presence.model';

@Component({
  selector: 'jhi-fiche-presence-detail',
  templateUrl: './fiche-presence-detail.component.html',
})
export class FichePresenceDetailComponent implements OnInit {
  fichePresence: IFichePresence | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichePresence }) => {
      this.fichePresence = fichePresence;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
