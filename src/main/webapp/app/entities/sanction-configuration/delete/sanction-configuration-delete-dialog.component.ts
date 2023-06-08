import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISanctionConfiguration } from '../sanction-configuration.model';
import { SanctionConfigurationService } from '../service/sanction-configuration.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sanction-configuration-delete-dialog.component.html',
})
export class SanctionConfigurationDeleteDialogComponent {
  sanctionConfiguration?: ISanctionConfiguration;

  constructor(protected sanctionConfigurationService: SanctionConfigurationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sanctionConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
