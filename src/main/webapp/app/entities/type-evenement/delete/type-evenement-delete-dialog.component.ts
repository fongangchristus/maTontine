import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeEvenement } from '../type-evenement.model';
import { TypeEvenementService } from '../service/type-evenement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './type-evenement-delete-dialog.component.html',
})
export class TypeEvenementDeleteDialogComponent {
  typeEvenement?: ITypeEvenement;

  constructor(protected typeEvenementService: TypeEvenementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeEvenementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
