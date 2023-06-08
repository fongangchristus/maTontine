import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaiementBanqueComponent } from '../list/paiement-banque.component';
import { PaiementBanqueDetailComponent } from '../detail/paiement-banque-detail.component';
import { PaiementBanqueUpdateComponent } from '../update/paiement-banque-update.component';
import { PaiementBanqueRoutingResolveService } from './paiement-banque-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paiementBanqueRoute: Routes = [
  {
    path: '',
    component: PaiementBanqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementBanqueDetailComponent,
    resolve: {
      paiementBanque: PaiementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementBanqueUpdateComponent,
    resolve: {
      paiementBanque: PaiementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementBanqueUpdateComponent,
    resolve: {
      paiementBanque: PaiementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paiementBanqueRoute)],
  exports: [RouterModule],
})
export class PaiementBanqueRoutingModule {}
