import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DocumentAssociationFormService, DocumentAssociationFormGroup } from './document-association-form.service';
import { IDocumentAssociation } from '../document-association.model';
import { DocumentAssociationService } from '../service/document-association.service';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';

@Component({
  selector: 'jhi-document-association-update',
  templateUrl: './document-association-update.component.html',
})
export class DocumentAssociationUpdateComponent implements OnInit {
  isSaving = false;
  documentAssociation: IDocumentAssociation | null = null;

  associationsSharedCollection: IAssociation[] = [];

  editForm: DocumentAssociationFormGroup = this.documentAssociationFormService.createDocumentAssociationFormGroup();

  constructor(
    protected documentAssociationService: DocumentAssociationService,
    protected documentAssociationFormService: DocumentAssociationFormService,
    protected associationService: AssociationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAssociation = (o1: IAssociation | null, o2: IAssociation | null): boolean => this.associationService.compareAssociation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentAssociation }) => {
      this.documentAssociation = documentAssociation;
      if (documentAssociation) {
        this.updateForm(documentAssociation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentAssociation = this.documentAssociationFormService.getDocumentAssociation(this.editForm);
    if (documentAssociation.id !== null) {
      this.subscribeToSaveResponse(this.documentAssociationService.update(documentAssociation));
    } else {
      this.subscribeToSaveResponse(this.documentAssociationService.create(documentAssociation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentAssociation>>): void {
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

  protected updateForm(documentAssociation: IDocumentAssociation): void {
    this.documentAssociation = documentAssociation;
    this.documentAssociationFormService.resetForm(this.editForm, documentAssociation);

    this.associationsSharedCollection = this.associationService.addAssociationToCollectionIfMissing<IAssociation>(
      this.associationsSharedCollection,
      documentAssociation.association
    );
  }

  protected loadRelationshipsOptions(): void {
    this.associationService
      .query()
      .pipe(map((res: HttpResponse<IAssociation[]>) => res.body ?? []))
      .pipe(
        map((associations: IAssociation[]) =>
          this.associationService.addAssociationToCollectionIfMissing<IAssociation>(associations, this.documentAssociation?.association)
        )
      )
      .subscribe((associations: IAssociation[]) => (this.associationsSharedCollection = associations));
  }
}
