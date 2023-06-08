import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompteBanqueComponent } from '../list/compte-banque.component';
import { CompteBanqueDetailComponent } from '../detail/compte-banque-detail.component';
import { CompteBanqueUpdateComponent } from '../update/compte-banque-update.component';
import { CompteBanqueRoutingResolveService } from './compte-banque-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const compteBanqueRoute: Routes = [
  {
    path: '',
    component: CompteBanqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompteBanqueDetailComponent,
    resolve: {
      compteBanque: CompteBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompteBanqueUpdateComponent,
    resolve: {
      compteBanque: CompteBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompteBanqueUpdateComponent,
    resolve: {
      compteBanque: CompteBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compteBanqueRoute)],
  exports: [RouterModule],
})
export class CompteBanqueRoutingModule {}
