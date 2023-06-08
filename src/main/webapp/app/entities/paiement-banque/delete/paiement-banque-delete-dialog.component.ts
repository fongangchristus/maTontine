import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaiementBanque } from '../paiement-banque.model';
import { PaiementBanqueService } from '../service/paiement-banque.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './paiement-banque-delete-dialog.component.html',
})
export class PaiementBanqueDeleteDialogComponent {
  paiementBanque?: IPaiementBanque;

  constructor(protected paiementBanqueService: PaiementBanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementBanqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
