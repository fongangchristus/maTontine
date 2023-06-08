import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaiementAdhesion } from '../paiement-adhesion.model';
import { PaiementAdhesionService } from '../service/paiement-adhesion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './paiement-adhesion-delete-dialog.component.html',
})
export class PaiementAdhesionDeleteDialogComponent {
  paiementAdhesion?: IPaiementAdhesion;

  constructor(protected paiementAdhesionService: PaiementAdhesionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementAdhesionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
