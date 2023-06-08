import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICotisationTontine } from '../cotisation-tontine.model';
import { CotisationTontineService } from '../service/cotisation-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cotisation-tontine-delete-dialog.component.html',
})
export class CotisationTontineDeleteDialogComponent {
  cotisationTontine?: ICotisationTontine;

  constructor(protected cotisationTontineService: CotisationTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cotisationTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
