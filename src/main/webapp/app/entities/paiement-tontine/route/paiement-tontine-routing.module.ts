import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaiementTontineComponent } from '../list/paiement-tontine.component';
import { PaiementTontineDetailComponent } from '../detail/paiement-tontine-detail.component';
import { PaiementTontineUpdateComponent } from '../update/paiement-tontine-update.component';
import { PaiementTontineRoutingResolveService } from './paiement-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paiementTontineRoute: Routes = [
  {
    path: '',
    component: PaiementTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementTontineDetailComponent,
    resolve: {
      paiementTontine: PaiementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementTontineUpdateComponent,
    resolve: {
      paiementTontine: PaiementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementTontineUpdateComponent,
    resolve: {
      paiementTontine: PaiementTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paiementTontineRoute)],
  exports: [RouterModule],
})
export class PaiementTontineRoutingModule {}
