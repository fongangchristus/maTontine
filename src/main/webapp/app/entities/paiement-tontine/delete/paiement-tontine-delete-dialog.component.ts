import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaiementTontine } from '../paiement-tontine.model';
import { PaiementTontineService } from '../service/paiement-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './paiement-tontine-delete-dialog.component.html',
})
export class PaiementTontineDeleteDialogComponent {
  paiementTontine?: IPaiementTontine;

  constructor(protected paiementTontineService: PaiementTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
