import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFonctionAdherent } from '../fonction-adherent.model';

@Component({
  selector: 'jhi-fonction-adherent-detail',
  templateUrl: './fonction-adherent-detail.component.html',
})
export class FonctionAdherentDetailComponent implements OnInit {
  fonctionAdherent: IFonctionAdherent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fonctionAdherent }) => {
      this.fonctionAdherent = fonctionAdherent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
