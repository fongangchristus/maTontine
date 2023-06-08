import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompteRIB } from '../compte-rib.model';
import { CompteRIBService } from '../service/compte-rib.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './compte-rib-delete-dialog.component.html',
})
export class CompteRIBDeleteDialogComponent {
  compteRIB?: ICompteRIB;

  constructor(protected compteRIBService: CompteRIBService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteRIBService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
