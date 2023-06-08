import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PotComponent } from '../list/pot.component';
import { PotDetailComponent } from '../detail/pot-detail.component';
import { PotUpdateComponent } from '../update/pot-update.component';
import { PotRoutingResolveService } from './pot-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const potRoute: Routes = [
  {
    path: '',
    component: PotComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PotDetailComponent,
    resolve: {
      pot: PotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PotUpdateComponent,
    resolve: {
      pot: PotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PotUpdateComponent,
    resolve: {
      pot: PotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(potRoute)],
  exports: [RouterModule],
})
export class PotRoutingModule {}
