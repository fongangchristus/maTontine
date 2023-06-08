import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypePotComponent } from '../list/type-pot.component';
import { TypePotDetailComponent } from '../detail/type-pot-detail.component';
import { TypePotUpdateComponent } from '../update/type-pot-update.component';
import { TypePotRoutingResolveService } from './type-pot-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const typePotRoute: Routes = [
  {
    path: '',
    component: TypePotComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypePotDetailComponent,
    resolve: {
      typePot: TypePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypePotUpdateComponent,
    resolve: {
      typePot: TypePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypePotUpdateComponent,
    resolve: {
      typePot: TypePotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typePotRoute)],
  exports: [RouterModule],
})
export class TypePotRoutingModule {}
