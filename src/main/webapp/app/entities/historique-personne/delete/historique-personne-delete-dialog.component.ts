import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHistoriquePersonne } from '../historique-personne.model';
import { HistoriquePersonneService } from '../service/historique-personne.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './historique-personne-delete-dialog.component.html',
})
export class HistoriquePersonneDeleteDialogComponent {
  historiquePersonne?: IHistoriquePersonne;

  constructor(protected historiquePersonneService: HistoriquePersonneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historiquePersonneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
