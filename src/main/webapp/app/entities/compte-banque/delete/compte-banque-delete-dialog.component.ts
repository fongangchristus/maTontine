import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompteBanque } from '../compte-banque.model';
import { CompteBanqueService } from '../service/compte-banque.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './compte-banque-delete-dialog.component.html',
})
export class CompteBanqueDeleteDialogComponent {
  compteBanque?: ICompteBanque;

  constructor(protected compteBanqueService: CompteBanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteBanqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
