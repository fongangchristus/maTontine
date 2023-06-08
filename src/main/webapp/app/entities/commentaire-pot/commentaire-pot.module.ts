import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommentairePotComponent } from './list/commentaire-pot.component';
import { CommentairePotDetailComponent } from './detail/commentaire-pot-detail.component';
import { CommentairePotUpdateComponent } from './update/commentaire-pot-update.component';
import { CommentairePotDeleteDialogComponent } from './delete/commentaire-pot-delete-dialog.component';
import { CommentairePotRoutingModule } from './route/commentaire-pot-routing.module';

@NgModule({
  imports: [SharedModule, CommentairePotRoutingModule],
  declarations: [
    CommentairePotComponent,
    CommentairePotDetailComponent,
    CommentairePotUpdateComponent,
    CommentairePotDeleteDialogComponent,
  ],
})
export class CommentairePotModule {}
