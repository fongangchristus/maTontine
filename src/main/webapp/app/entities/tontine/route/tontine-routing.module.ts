import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TontineComponent } from '../list/tontine.component';
import { TontineDetailComponent } from '../detail/tontine-detail.component';
import { TontineUpdateComponent } from '../update/tontine-update.component';
import { TontineRoutingResolveService } from './tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tontineRoute: Routes = [
  {
    path: '',
    component: TontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TontineDetailComponent,
    resolve: {
      tontine: TontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TontineUpdateComponent,
    resolve: {
      tontine: TontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TontineUpdateComponent,
    resolve: {
      tontine: TontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tontineRoute)],
  exports: [RouterModule],
})
export class TontineRoutingModule {}
