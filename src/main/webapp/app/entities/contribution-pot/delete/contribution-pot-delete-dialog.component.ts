import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContributionPot } from '../contribution-pot.model';
import { ContributionPotService } from '../service/contribution-pot.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './contribution-pot-delete-dialog.component.html',
})
export class ContributionPotDeleteDialogComponent {
  contributionPot?: IContributionPot;

  constructor(protected contributionPotService: ContributionPotService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contributionPotService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
