import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISessionTontine } from '../session-tontine.model';

@Component({
  selector: 'jhi-session-tontine-detail',
  templateUrl: './session-tontine-detail.component.html',
})
export class SessionTontineDetailComponent implements OnInit {
  sessionTontine: ISessionTontine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionTontine }) => {
      this.sessionTontine = sessionTontine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
