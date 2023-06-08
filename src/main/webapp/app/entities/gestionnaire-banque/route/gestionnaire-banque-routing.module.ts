import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GestionnaireBanqueComponent } from '../list/gestionnaire-banque.component';
import { GestionnaireBanqueDetailComponent } from '../detail/gestionnaire-banque-detail.component';
import { GestionnaireBanqueUpdateComponent } from '../update/gestionnaire-banque-update.component';
import { GestionnaireBanqueRoutingResolveService } from './gestionnaire-banque-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gestionnaireBanqueRoute: Routes = [
  {
    path: '',
    component: GestionnaireBanqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GestionnaireBanqueDetailComponent,
    resolve: {
      gestionnaireBanque: GestionnaireBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GestionnaireBanqueUpdateComponent,
    resolve: {
      gestionnaireBanque: GestionnaireBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GestionnaireBanqueUpdateComponent,
    resolve: {
      gestionnaireBanque: GestionnaireBanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gestionnaireBanqueRoute)],
  exports: [RouterModule],
})
export class GestionnaireBanqueRoutingModule {}
