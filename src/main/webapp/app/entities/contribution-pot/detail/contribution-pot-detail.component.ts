import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContributionPot } from '../contribution-pot.model';

@Component({
  selector: 'jhi-contribution-pot-detail',
  templateUrl: './contribution-pot-detail.component.html',
})
export class ContributionPotDetailComponent implements OnInit {
  contributionPot: IContributionPot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contributionPot }) => {
      this.contributionPot = contributionPot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
