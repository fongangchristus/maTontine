import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaiementAdhesionComponent } from '../list/paiement-adhesion.component';
import { PaiementAdhesionDetailComponent } from '../detail/paiement-adhesion-detail.component';
import { PaiementAdhesionUpdateComponent } from '../update/paiement-adhesion-update.component';
import { PaiementAdhesionRoutingResolveService } from './paiement-adhesion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paiementAdhesionRoute: Routes = [
  {
    path: '',
    component: PaiementAdhesionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementAdhesionDetailComponent,
    resolve: {
      paiementAdhesion: PaiementAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementAdhesionUpdateComponent,
    resolve: {
      paiementAdhesion: PaiementAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementAdhesionUpdateComponent,
    resolve: {
      paiementAdhesion: PaiementAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paiementAdhesionRoute)],
  exports: [RouterModule],
})
export class PaiementAdhesionRoutingModule {}
