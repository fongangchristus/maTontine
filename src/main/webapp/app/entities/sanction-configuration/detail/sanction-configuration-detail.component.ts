import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISanctionConfiguration } from '../sanction-configuration.model';

@Component({
  selector: 'jhi-sanction-configuration-detail',
  templateUrl: './sanction-configuration-detail.component.html',
})
export class SanctionConfigurationDetailComponent implements OnInit {
  sanctionConfiguration: ISanctionConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanctionConfiguration }) => {
      this.sanctionConfiguration = sanctionConfiguration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
