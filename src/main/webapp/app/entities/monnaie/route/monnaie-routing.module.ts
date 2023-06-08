import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MonnaieComponent } from '../list/monnaie.component';
import { MonnaieDetailComponent } from '../detail/monnaie-detail.component';
import { MonnaieUpdateComponent } from '../update/monnaie-update.component';
import { MonnaieRoutingResolveService } from './monnaie-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const monnaieRoute: Routes = [
  {
    path: '',
    component: MonnaieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MonnaieDetailComponent,
    resolve: {
      monnaie: MonnaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MonnaieUpdateComponent,
    resolve: {
      monnaie: MonnaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MonnaieUpdateComponent,
    resolve: {
      monnaie: MonnaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(monnaieRoute)],
  exports: [RouterModule],
})
export class MonnaieRoutingModule {}
