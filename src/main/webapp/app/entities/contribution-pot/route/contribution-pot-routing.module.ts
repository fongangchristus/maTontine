import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContributionPotComponent } from '../list/contribution-pot.component';
import { ContributionPotDetailComponent } from '../detail/contribution-pot-detail.component';
import { ContributionPotUpdateComponent } from '../update/contribution-pot-update.component';
import { ContributionPotRoutingResolveService } from './contribution-pot-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contributionPotRoute: Routes = [
  {
    path: '',
    component: ContributionPotComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContributionPotDetailComponent,
    resolve: {
      contributionPot: ContributionPotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContributionPotUpdateComponent,
    resolve: {
      contributionPot: ContributionPotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContributionPotUpdateComponent,
    resolve: {
      contributionPot: ContributionPotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contributionPotRoute)],
  exports: [RouterModule],
})
export class ContributionPotRoutingModule {}
