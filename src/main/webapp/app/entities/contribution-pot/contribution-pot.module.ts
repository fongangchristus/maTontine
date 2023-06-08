import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContributionPotComponent } from './list/contribution-pot.component';
import { ContributionPotDetailComponent } from './detail/contribution-pot-detail.component';
import { ContributionPotUpdateComponent } from './update/contribution-pot-update.component';
import { ContributionPotDeleteDialogComponent } from './delete/contribution-pot-delete-dialog.component';
import { ContributionPotRoutingModule } from './route/contribution-pot-routing.module';

@NgModule({
  imports: [SharedModule, ContributionPotRoutingModule],
  declarations: [
    ContributionPotComponent,
    ContributionPotDetailComponent,
    ContributionPotUpdateComponent,
    ContributionPotDeleteDialogComponent,
  ],
})
export class ContributionPotModule {}
