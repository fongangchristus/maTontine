import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SanctionConfigurationFormService, SanctionConfigurationFormGroup } from './sanction-configuration-form.service';
import { ISanctionConfiguration } from '../sanction-configuration.model';
import { SanctionConfigurationService } from '../service/sanction-configuration.service';
import { TypeSanction } from 'app/entities/enumerations/type-sanction.model';

@Component({
  selector: 'jhi-sanction-configuration-update',
  templateUrl: './sanction-configuration-update.component.html',
})
export class SanctionConfigurationUpdateComponent implements OnInit {
  isSaving = false;
  sanctionConfiguration: ISanctionConfiguration | null = null;
  typeSanctionValues = Object.keys(TypeSanction);

  editForm: SanctionConfigurationFormGroup = this.sanctionConfigurationFormService.createSanctionConfigurationFormGroup();

  constructor(
    protected sanctionConfigurationService: SanctionConfigurationService,
    protected sanctionConfigurationFormService: SanctionConfigurationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanctionConfiguration }) => {
      this.sanctionConfiguration = sanctionConfiguration;
      if (sanctionConfiguration) {
        this.updateForm(sanctionConfiguration);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sanctionConfiguration = this.sanctionConfigurationFormService.getSanctionConfiguration(this.editForm);
    if (sanctionConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.sanctionConfigurationService.update(sanctionConfiguration));
    } else {
      this.subscribeToSaveResponse(this.sanctionConfigurationService.create(sanctionConfiguration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISanctionConfiguration>>): void {
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

  protected updateForm(sanctionConfiguration: ISanctionConfiguration): void {
    this.sanctionConfiguration = sanctionConfiguration;
    this.sanctionConfigurationFormService.resetForm(this.editForm, sanctionConfiguration);
  }
}
