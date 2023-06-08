import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDecaissementTontine } from '../decaissement-tontine.model';
import { DecaissementTontineService } from '../service/decaissement-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './decaissement-tontine-delete-dialog.component.html',
})
export class DecaissementTontineDeleteDialogComponent {
  decaissementTontine?: IDecaissementTontine;

  constructor(protected decaissementTontineService: DecaissementTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decaissementTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
