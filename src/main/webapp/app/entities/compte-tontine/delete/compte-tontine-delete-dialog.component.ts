import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompteTontine } from '../compte-tontine.model';
import { CompteTontineService } from '../service/compte-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './compte-tontine-delete-dialog.component.html',
})
export class CompteTontineDeleteDialogComponent {
  compteTontine?: ICompteTontine;

  constructor(protected compteTontineService: CompteTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
