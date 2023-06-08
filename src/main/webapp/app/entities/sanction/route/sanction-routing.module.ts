import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SanctionComponent } from '../list/sanction.component';
import { SanctionDetailComponent } from '../detail/sanction-detail.component';
import { SanctionUpdateComponent } from '../update/sanction-update.component';
import { SanctionRoutingResolveService } from './sanction-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sanctionRoute: Routes = [
  {
    path: '',
    component: SanctionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SanctionDetailComponent,
    resolve: {
      sanction: SanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SanctionUpdateComponent,
    resolve: {
      sanction: SanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SanctionUpdateComponent,
    resolve: {
      sanction: SanctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sanctionRoute)],
  exports: [RouterModule],
})
export class SanctionRoutingModule {}
