import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DecaisementBanqueComponent } from '../list/decaisement-banque.component';
import { DecaisementBanqueDetailComponent } from '../detail/decaisement-banque-detail.component';
import { DecaisementBanqueUpdateComponent } from '../update/decaisement-banque-update.component';
import { DecaisementBanqueRoutingResolveService } from './decaisement-banque-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const decaisementBanqueRoute: Routes = [
  {
    path: '',
    component: DecaisementBanqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DecaisementBanqueDetailComponent,
    resolve: {
      decaisementBanque: DecaisementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DecaisementBanqueUpdateComponent,
    resolve: {
      decaisementBanque: DecaisementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DecaisementBanqueUpdateComponent,
    resolve: {
      decaisementBanque: DecaisementBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(decaisementBanqueRoute)],
  exports: [RouterModule],
})
export class DecaisementBanqueRoutingModule {}
