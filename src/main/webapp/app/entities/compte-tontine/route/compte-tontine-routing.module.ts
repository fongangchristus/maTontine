import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompteTontineComponent } from '../list/compte-tontine.component';
import { CompteTontineDetailComponent } from '../detail/compte-tontine-detail.component';
import { CompteTontineUpdateComponent } from '../update/compte-tontine-update.component';
import { CompteTontineRoutingResolveService } from './compte-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const compteTontineRoute: Routes = [
  {
    path: '',
    component: CompteTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompteTontineDetailComponent,
    resolve: {
      compteTontine: CompteTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompteTontineUpdateComponent,
    resolve: {
      compteTontine: CompteTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompteTontineUpdateComponent,
    resolve: {
      compteTontine: CompteTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compteTontineRoute)],
  exports: [RouterModule],
})
export class CompteTontineRoutingModule {}
