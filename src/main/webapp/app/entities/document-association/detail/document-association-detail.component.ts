import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentAssociation } from '../document-association.model';

@Component({
  selector: 'jhi-document-association-detail',
  templateUrl: './document-association-detail.component.html',
})
export class DocumentAssociationDetailComponent implements OnInit {
  documentAssociation: IDocumentAssociation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentAssociation }) => {
      this.documentAssociation = documentAssociation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
