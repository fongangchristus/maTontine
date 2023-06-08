import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommentairePotComponent } from '../list/commentaire-pot.component';
import { CommentairePotDetailComponent } from '../detail/commentaire-pot-detail.component';
import { CommentairePotUpdateComponent } from '../update/commentaire-pot-update.component';
import { CommentairePotRoutingResolveService } from './commentaire-pot-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const commentairePotRoute: Routes = [
  {
    path: '',
    component: CommentairePotComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommentairePotDetailComponent,
    resolve: {
      commentairePot: CommentairePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommentairePotUpdateComponent,
    resolve: {
      commentairePot: CommentairePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommentairePotUpdateComponent,
    resolve: {
      commentairePot: CommentairePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commentairePotRoute)],
  exports: [RouterModule],
})
export class CommentairePotRoutingModule {}
