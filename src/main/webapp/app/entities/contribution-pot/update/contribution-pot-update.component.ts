import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContributionPotFormService, ContributionPotFormGroup } from './contribution-pot-form.service';
import { IContributionPot } from '../contribution-pot.model';
import { ContributionPotService } from '../service/contribution-pot.service';
import { IPot } from 'app/entities/pot/pot.model';
import { PotService } from 'app/entities/pot/service/pot.service';

@Component({
  selector: 'jhi-contribution-pot-update',
  templateUrl: './contribution-pot-update.component.html',
})
export class ContributionPotUpdateComponent implements OnInit {
  isSaving = false;
  contributionPot: IContributionPot | null = null;

  potsSharedCollection: IPot[] = [];

  editForm: ContributionPotFormGroup = this.contributionPotFormService.createContributionPotFormGroup();

  constructor(
    protected contributionPotService: ContributionPotService,
    protected contributionPotFormService: ContributionPotFormService,
    protected potService: PotService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePot = (o1: IPot | null, o2: IPot | null): boolean => this.potService.comparePot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contributionPot }) => {
      this.contributionPot = contributionPot;
      if (contributionPot) {
        this.updateForm(contributionPot);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contributionPot = this.contributionPotFormService.getContributionPot(this.editForm);
    if (contributionPot.id !== null) {
      this.subscribeToSaveResponse(this.contributionPotService.update(contributionPot));
    } else {
      this.subscribeToSaveResponse(this.contributionPotService.create(contributionPot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContributionPot>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contributionPot: IContributionPot): void {
    this.contributionPot = contributionPot;
    this.contributionPotFormService.resetForm(this.editForm, contributionPot);

    this.potsSharedCollection = this.potService.addPotToCollectionIfMissing<IPot>(this.potsSharedCollection, contributionPot.pot);
  }

  protected loadRelationshipsOptions(): void {
    this.potService
      .query()
      .pipe(map((res: HttpResponse<IPot[]>) => res.body ?? []))
      .pipe(map((pots: IPot[]) => this.potService.addPotToCollectionIfMissing<IPot>(pots, this.contributionPot?.pot)))
      .subscribe((pots: IPot[]) => (this.potsSharedCollection = pots));
  }
}
