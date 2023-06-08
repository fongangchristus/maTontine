import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { GestionnaireTontineService } from '../service/gestionnaire-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './gestionnaire-tontine-delete-dialog.component.html',
})
export class GestionnaireTontineDeleteDialogComponent {
  gestionnaireTontine?: IGestionnaireTontine;

  constructor(protected gestionnaireTontineService: GestionnaireTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gestionnaireTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
