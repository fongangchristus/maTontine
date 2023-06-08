import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompteRIBComponent } from '../list/compte-rib.component';
import { CompteRIBDetailComponent } from '../detail/compte-rib-detail.component';
import { CompteRIBUpdateComponent } from '../update/compte-rib-update.component';
import { CompteRIBRoutingResolveService } from './compte-rib-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const compteRIBRoute: Routes = [
  {
    path: '',
    component: CompteRIBComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompteRIBDetailComponent,
    resolve: {
      compteRIB: CompteRIBRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompteRIBUpdateComponent,
    resolve: {
      compteRIB: CompteRIBRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompteRIBUpdateComponent,
    resolve: {
      compteRIB: CompteRIBRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compteRIBRoute)],
  exports: [RouterModule],
})
export class CompteRIBRoutingModule {}
