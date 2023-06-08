import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { HistoriquePersonneFormService, HistoriquePersonneFormGroup } from './historique-personne-form.service';
import { IHistoriquePersonne } from '../historique-personne.model';
import { HistoriquePersonneService } from '../service/historique-personne.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-historique-personne-update',
  templateUrl: './historique-personne-update.component.html',
})
export class HistoriquePersonneUpdateComponent implements OnInit {
  isSaving = false;
  historiquePersonne: IHistoriquePersonne | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: HistoriquePersonneFormGroup = this.historiquePersonneFormService.createHistoriquePersonneFormGroup();

  constructor(
    protected historiquePersonneService: HistoriquePersonneService,
    protected historiquePersonneFormService: HistoriquePersonneFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiquePersonne }) => {
      this.historiquePersonne = historiquePersonne;
      if (historiquePersonne) {
        this.updateForm(historiquePersonne);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historiquePersonne = this.historiquePersonneFormService.getHistoriquePersonne(this.editForm);
    if (historiquePersonne.id !== null) {
      this.subscribeToSaveResponse(this.historiquePersonneService.update(historiquePersonne));
    } else {
      this.subscribeToSaveResponse(this.historiquePersonneService.create(historiquePersonne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoriquePersonne>>): void {
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

  protected updateForm(historiquePersonne: IHistoriquePersonne): void {
    this.historiquePersonne = historiquePersonne;
    this.historiquePersonneFormService.resetForm(this.editForm, historiquePersonne);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      historiquePersonne.personne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.historiquePersonne?.personne)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
