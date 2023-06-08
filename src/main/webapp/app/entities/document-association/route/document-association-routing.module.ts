import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentAssociationComponent } from '../list/document-association.component';
import { DocumentAssociationDetailComponent } from '../detail/document-association-detail.component';
import { DocumentAssociationUpdateComponent } from '../update/document-association-update.component';
import { DocumentAssociationRoutingResolveService } from './document-association-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const documentAssociationRoute: Routes = [
  {
    path: '',
    component: DocumentAssociationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentAssociationDetailComponent,
    resolve: {
      documentAssociation: DocumentAssociationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentAssociationUpdateComponent,
    resolve: {
      documentAssociation: DocumentAssociationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentAssociationUpdateComponent,
    resolve: {
      documentAssociation: DocumentAssociationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentAssociationRoute)],
  exports: [RouterModule],
})
export class DocumentAssociationRoutingModule {}
