import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDecaisementBanque } from '../decaisement-banque.model';
import { DecaisementBanqueService } from '../service/decaisement-banque.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './decaisement-banque-delete-dialog.component.html',
})
export class DecaisementBanqueDeleteDialogComponent {
  decaisementBanque?: IDecaisementBanque;

  constructor(protected decaisementBanqueService: DecaisementBanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decaisementBanqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
