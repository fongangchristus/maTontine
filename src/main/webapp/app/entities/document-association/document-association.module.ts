import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentAssociationComponent } from './list/document-association.component';
import { DocumentAssociationDetailComponent } from './detail/document-association-detail.component';
import { DocumentAssociationUpdateComponent } from './update/document-association-update.component';
import { DocumentAssociationDeleteDialogComponent } from './delete/document-association-delete-dialog.component';
import { DocumentAssociationRoutingModule } from './route/document-association-routing.module';

@NgModule({
  imports: [SharedModule, DocumentAssociationRoutingModule],
  declarations: [
    DocumentAssociationComponent,
    DocumentAssociationDetailComponent,
    DocumentAssociationUpdateComponent,
    DocumentAssociationDeleteDialogComponent,
  ],
})
export class DocumentAssociationModule {}
