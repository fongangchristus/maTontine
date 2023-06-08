import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HistoriquePersonneComponent } from '../list/historique-personne.component';
import { HistoriquePersonneDetailComponent } from '../detail/historique-personne-detail.component';
import { HistoriquePersonneUpdateComponent } from '../update/historique-personne-update.component';
import { HistoriquePersonneRoutingResolveService } from './historique-personne-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const historiquePersonneRoute: Routes = [
  {
    path: '',
    component: HistoriquePersonneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoriquePersonneDetailComponent,
    resolve: {
      historiquePersonne: HistoriquePersonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoriquePersonneUpdateComponent,
    resolve: {
      historiquePersonne: HistoriquePersonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoriquePersonneUpdateComponent,
    resolve: {
      historiquePersonne: HistoriquePersonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(historiquePersonneRoute)],
  exports: [RouterModule],
})
export class HistoriquePersonneRoutingModule {}
