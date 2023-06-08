import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommentairePot } from '../commentaire-pot.model';
import { CommentairePotService } from '../service/commentaire-pot.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './commentaire-pot-delete-dialog.component.html',
})
export class CommentairePotDeleteDialogComponent {
  commentairePot?: ICommentairePot;

  constructor(protected commentairePotService: CommentairePotService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commentairePotService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
