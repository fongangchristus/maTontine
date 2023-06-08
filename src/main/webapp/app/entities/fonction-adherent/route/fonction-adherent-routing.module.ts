import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FonctionAdherentComponent } from '../list/fonction-adherent.component';
import { FonctionAdherentDetailComponent } from '../detail/fonction-adherent-detail.component';
import { FonctionAdherentUpdateComponent } from '../update/fonction-adherent-update.component';
import { FonctionAdherentRoutingResolveService } from './fonction-adherent-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fonctionAdherentRoute: Routes = [
  {
    path: '',
    component: FonctionAdherentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FonctionAdherentDetailComponent,
    resolve: {
      fonctionAdherent: FonctionAdherentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FonctionAdherentUpdateComponent,
    resolve: {
      fonctionAdherent: FonctionAdherentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FonctionAdherentUpdateComponent,
    resolve: {
      fonctionAdherent: FonctionAdherentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fonctionAdherentRoute)],
  exports: [RouterModule],
})
export class FonctionAdherentRoutingModule {}
