import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GestionnaireTontineComponent } from '../list/gestionnaire-tontine.component';
import { GestionnaireTontineDetailComponent } from '../detail/gestionnaire-tontine-detail.component';
import { GestionnaireTontineUpdateComponent } from '../update/gestionnaire-tontine-update.component';
import { GestionnaireTontineRoutingResolveService } from './gestionnaire-tontine-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gestionnaireTontineRoute: Routes = [
  {
    path: '',
    component: GestionnaireTontineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GestionnaireTontineDetailComponent,
    resolve: {
      gestionnaireTontine: GestionnaireTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GestionnaireTontineUpdateComponent,
    resolve: {
      gestionnaireTontine: GestionnaireTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GestionnaireTontineUpdateComponent,
    resolve: {
      gestionnaireTontine: GestionnaireTontineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gestionnaireTontineRoute)],
  exports: [RouterModule],
})
export class GestionnaireTontineRoutingModule {}
