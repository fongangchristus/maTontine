import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SanctionFormService, SanctionFormGroup } from './sanction-form.service';
import { ISanction } from '../sanction.model';
import { SanctionService } from '../service/sanction.service';
import { ISanctionConfiguration } from 'app/entities/sanction-configuration/sanction-configuration.model';
import { SanctionConfigurationService } from 'app/entities/sanction-configuration/service/sanction-configuration.service';

@Component({
  selector: 'jhi-sanction-update',
  templateUrl: './sanction-update.component.html',
})
export class SanctionUpdateComponent implements OnInit {
  isSaving = false;
  sanction: ISanction | null = null;

  sanctionConfigurationsSharedCollection: ISanctionConfiguration[] = [];

  editForm: SanctionFormGroup = this.sanctionFormService.createSanctionFormGroup();

  constructor(
    protected sanctionService: SanctionService,
    protected sanctionFormService: SanctionFormService,
    protected sanctionConfigurationService: SanctionConfigurationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSanctionConfiguration = (o1: ISanctionConfiguration | null, o2: ISanctionConfiguration | null): boolean =>
    this.sanctionConfigurationService.compareSanctionConfiguration(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanction }) => {
      this.sanction = sanction;
      if (sanction) {
        this.updateForm(sanction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sanction = this.sanctionFormService.getSanction(this.editForm);
    if (sanction.id !== null) {
      this.subscribeToSaveResponse(this.sanctionService.update(sanction));
    } else {
      this.subscribeToSaveResponse(this.sanctionService.create(sanction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISanction>>): void {
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

  protected updateForm(sanction: ISanction): void {
    this.sanction = sanction;
    this.sanctionFormService.resetForm(this.editForm, sanction);

    this.sanctionConfigurationsSharedCollection =
      this.sanctionConfigurationService.addSanctionConfigurationToCollectionIfMissing<ISanctionConfiguration>(
        this.sanctionConfigurationsSharedCollection,
        sanction.sanctionConfig
      );
  }

  protected loadRelationshipsOptions(): void {
    this.sanctionConfigurationService
      .query()
      .pipe(map((res: HttpResponse<ISanctionConfiguration[]>) => res.body ?? []))
      .pipe(
        map((sanctionConfigurations: ISanctionConfiguration[]) =>
          this.sanctionConfigurationService.addSanctionConfigurationToCollectionIfMissing<ISanctionConfiguration>(
            sanctionConfigurations,
            this.sanction?.sanctionConfig
          )
        )
      )
      .subscribe(
        (sanctionConfigurations: ISanctionConfiguration[]) => (this.sanctionConfigurationsSharedCollection = sanctionConfigurations)
      );
  }
}
