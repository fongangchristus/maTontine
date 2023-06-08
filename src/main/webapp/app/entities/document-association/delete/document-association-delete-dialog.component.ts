import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentAssociation } from '../document-association.model';
import { DocumentAssociationService } from '../service/document-association.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './document-association-delete-dialog.component.html',
})
export class DocumentAssociationDeleteDialogComponent {
  documentAssociation?: IDocumentAssociation;

  constructor(protected documentAssociationService: DocumentAssociationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentAssociationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
