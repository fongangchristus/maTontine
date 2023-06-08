import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SessionTontineComponent } from '../list/session-tontine.component';
import { SessionTontineDetailComponent } from '../detail/session-tontine-detail.component';
import { SessionTontineUpdateComponent } from '../update/session-tontine-update.component';
import { SessionTontineRoutingResolveService } from './session-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sessionTontineRoute: Routes = [
  {
    path: '',
    component: SessionTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionTontineDetailComponent,
    resolve: {
      sessionTontine: SessionTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionTontineUpdateComponent,
    resolve: {
      sessionTontine: SessionTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionTontineUpdateComponent,
    resolve: {
      sessionTontine: SessionTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sessionTontineRoute)],
  exports: [RouterModule],
})
export class SessionTontineRoutingModule {}
