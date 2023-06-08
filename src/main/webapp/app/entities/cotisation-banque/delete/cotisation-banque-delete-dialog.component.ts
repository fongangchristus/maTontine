import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICotisationBanque } from '../cotisation-banque.model';
import { CotisationBanqueService } from '../service/cotisation-banque.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cotisation-banque-delete-dialog.component.html',
})
export class CotisationBanqueDeleteDialogComponent {
  cotisationBanque?: ICotisationBanque;

  constructor(protected cotisationBanqueService: CotisationBanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cotisationBanqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
