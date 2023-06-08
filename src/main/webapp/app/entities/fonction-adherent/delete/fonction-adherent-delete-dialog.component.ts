import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFonctionAdherent } from '../fonction-adherent.model';
import { FonctionAdherentService } from '../service/fonction-adherent.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fonction-adherent-delete-dialog.component.html',
})
export class FonctionAdherentDeleteDialogComponent {
  fonctionAdherent?: IFonctionAdherent;

  constructor(protected fonctionAdherentService: FonctionAdherentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fonctionAdherentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
