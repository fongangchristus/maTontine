import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormuleAdhesionComponent } from '../list/formule-adhesion.component';
import { FormuleAdhesionDetailComponent } from '../detail/formule-adhesion-detail.component';
import { FormuleAdhesionUpdateComponent } from '../update/formule-adhesion-update.component';
import { FormuleAdhesionRoutingResolveService } from './formule-adhesion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const formuleAdhesionRoute: Routes = [
  {
    path: '',
    component: FormuleAdhesionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormuleAdhesionDetailComponent,
    resolve: {
      formuleAdhesion: FormuleAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormuleAdhesionUpdateComponent,
    resolve: {
      formuleAdhesion: FormuleAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormuleAdhesionUpdateComponent,
    resolve: {
      formuleAdhesion: FormuleAdhesionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formuleAdhesionRoute)],
  exports: [RouterModule],
})
export class FormuleAdhesionRoutingModule {}
