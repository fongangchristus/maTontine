import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormuleAdhesion } from '../formule-adhesion.model';
import { FormuleAdhesionService } from '../service/formule-adhesion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './formule-adhesion-delete-dialog.component.html',
})
export class FormuleAdhesionDeleteDialogComponent {
  formuleAdhesion?: IFormuleAdhesion;

  constructor(protected formuleAdhesionService: FormuleAdhesionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formuleAdhesionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
