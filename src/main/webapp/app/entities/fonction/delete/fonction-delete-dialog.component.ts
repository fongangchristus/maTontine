import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFonction } from '../fonction.model';
import { FonctionService } from '../service/fonction.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fonction-delete-dialog.component.html',
})
export class FonctionDeleteDialogComponent {
  fonction?: IFonction;

  constructor(protected fonctionService: FonctionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fonctionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
