import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISessionTontine } from '../session-tontine.model';
import { SessionTontineService } from '../service/session-tontine.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './session-tontine-delete-dialog.component.html',
})
export class SessionTontineDeleteDialogComponent {
  sessionTontine?: ISessionTontine;

  constructor(protected sessionTontineService: SessionTontineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionTontineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
