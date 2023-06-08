import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CotisationTontineComponent } from '../list/cotisation-tontine.component';
import { CotisationTontineDetailComponent } from '../detail/cotisation-tontine-detail.component';
import { CotisationTontineUpdateComponent } from '../update/cotisation-tontine-update.component';
import { CotisationTontineRoutingResolveService } from './cotisation-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cotisationTontineRoute: Routes = [
  {
    path: '',
    component: CotisationTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CotisationTontineDetailComponent,
    resolve: {
      cotisationTontine: CotisationTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CotisationTontineUpdateComponent,
    resolve: {
      cotisationTontine: CotisationTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CotisationTontineUpdateComponent,
    resolve: {
      cotisationTontine: CotisationTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cotisationTontineRoute)],
  exports: [RouterModule],
})
export class CotisationTontineRoutingModule {}
