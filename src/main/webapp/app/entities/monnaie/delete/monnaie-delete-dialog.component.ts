import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMonnaie } from '../monnaie.model';
import { MonnaieService } from '../service/monnaie.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './monnaie-delete-dialog.component.html',
})
export class MonnaieDeleteDialogComponent {
  monnaie?: IMonnaie;

  constructor(protected monnaieService: MonnaieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monnaieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
