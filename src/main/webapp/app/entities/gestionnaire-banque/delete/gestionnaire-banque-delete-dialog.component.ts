import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGestionnaireBanque } from '../gestionnaire-banque.model';
import { GestionnaireBanqueService } from '../service/gestionnaire-banque.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './gestionnaire-banque-delete-dialog.component.html',
})
export class GestionnaireBanqueDeleteDialogComponent {
  gestionnaireBanque?: IGestionnaireBanque;

  constructor(protected gestionnaireBanqueService: GestionnaireBanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gestionnaireBanqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
