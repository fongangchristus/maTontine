import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SanctionConfigurationComponent } from '../list/sanction-configuration.component';
import { SanctionConfigurationDetailComponent } from '../detail/sanction-configuration-detail.component';
import { SanctionConfigurationUpdateComponent } from '../update/sanction-configuration-update.component';
import { SanctionConfigurationRoutingResolveService } from './sanction-configuration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sanctionConfigurationRoute: Routes = [
  {
    path: '',
    component: SanctionConfigurationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SanctionConfigurationDetailComponent,
    resolve: {
      sanctionConfiguration: SanctionConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SanctionConfigurationUpdateComponent,
    resolve: {
      sanctionConfiguration: SanctionConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SanctionConfigurationUpdateComponent,
    resolve: {
      sanctionConfiguration: SanctionConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sanctionConfigurationRoute)],
  exports: [RouterModule],
})
export class SanctionConfigurationRoutingModule {}
