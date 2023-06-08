import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaiementSanctionComponent } from '../list/paiement-sanction.component';
import { PaiementSanctionDetailComponent } from '../detail/paiement-sanction-detail.component';
import { PaiementSanctionUpdateComponent } from '../update/paiement-sanction-update.component';
import { PaiementSanctionRoutingResolveService } from './paiement-sanction-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paiementSanctionRoute: Routes = [
  {
    path: '',
    component: PaiementSanctionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementSanctionDetailComponent,
    resolve: {
      paiementSanction: PaiementSanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementSanctionUpdateComponent,
    resolve: {
      paiementSanction: PaiementSanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementSanctionUpdateComponent,
    resolve: {
      paiementSanction: PaiementSanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paiementSanctionRoute)],
  exports: [RouterModule],
})
export class PaiementSanctionRoutingModule {}
