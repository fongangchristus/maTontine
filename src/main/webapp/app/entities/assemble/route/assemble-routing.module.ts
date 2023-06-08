import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssembleComponent } from '../list/assemble.component';
import { AssembleDetailComponent } from '../detail/assemble-detail.component';
import { AssembleUpdateComponent } from '../update/assemble-update.component';
import { AssembleRoutingResolveService } from './assemble-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const assembleRoute: Routes = [
  {
    path: '',
    component: AssembleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssembleDetailComponent,
    resolve: {
      assemble: AssembleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssembleUpdateComponent,
    resolve: {
      assemble: AssembleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssembleUpdateComponent,
    resolve: {
      assemble: AssembleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assembleRoute)],
  exports: [RouterModule],
})
export class AssembleRoutingModule {}
