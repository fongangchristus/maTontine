import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CotisationBanqueComponent } from '../list/cotisation-banque.component';
import { CotisationBanqueDetailComponent } from '../detail/cotisation-banque-detail.component';
import { CotisationBanqueUpdateComponent } from '../update/cotisation-banque-update.component';
import { CotisationBanqueRoutingResolveService } from './cotisation-banque-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cotisationBanqueRoute: Routes = [
  {
    path: '',
    component: CotisationBanqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CotisationBanqueDetailComponent,
    resolve: {
      cotisationBanque: CotisationBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CotisationBanqueUpdateComponent,
    resolve: {
      cotisationBanque: CotisationBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CotisationBanqueUpdateComponent,
    resolve: {
      cotisationBanque: CotisationBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cotisationBanqueRoute)],
  exports: [RouterModule],
})
export class CotisationBanqueRoutingModule {}
