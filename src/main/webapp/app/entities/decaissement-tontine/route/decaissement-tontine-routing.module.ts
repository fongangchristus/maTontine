import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DecaissementTontineComponent } from '../list/decaissement-tontine.component';
import { DecaissementTontineDetailComponent } from '../detail/decaissement-tontine-detail.component';
import { DecaissementTontineUpdateComponent } from '../update/decaissement-tontine-update.component';
import { DecaissementTontineRoutingResolveService } from './decaissement-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const decaissementTontineRoute: Routes = [
  {
    path: '',
    component: DecaissementTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DecaissementTontineDetailComponent,
    resolve: {
      decaissementTontine: DecaissementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DecaissementTontineUpdateComponent,
    resolve: {
      decaissementTontine: DecaissementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DecaissementTontineUpdateComponent,
    resolve: {
      decaissementTontine: DecaissementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(decaissementTontineRoute)],
  exports: [RouterModule],
})
export class DecaissementTontineRoutingModule {}
