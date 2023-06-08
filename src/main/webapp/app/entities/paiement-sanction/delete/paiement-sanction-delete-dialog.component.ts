import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaiementSanction } from '../paiement-sanction.model';
import { PaiementSanctionService } from '../service/paiement-sanction.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './paiement-sanction-delete-dialog.component.html',
})
export class PaiementSanctionDeleteDialogComponent {
  paiementSanction?: IPaiementSanction;

  constructor(protected paiementSanctionService: PaiementSanctionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementSanctionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
