import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SanctionConfigurationComponent } from './list/sanction-configuration.component';
import { SanctionConfigurationDetailComponent } from './detail/sanction-configuration-detail.component';
import { SanctionConfigurationUpdateComponent } from './update/sanction-configuration-update.component';
import { SanctionConfigurationDeleteDialogComponent } from './delete/sanction-configuration-delete-dialog.component';
import { SanctionConfigurationRoutingModule } from './route/sanction-configuration-routing.module';

@NgModule({
  imports: [SharedModule, SanctionConfigurationRoutingModule],
  declarations: [
    SanctionConfigurationComponent,
    SanctionConfigurationDetailComponent,
    SanctionConfigurationUpdateComponent,
    SanctionConfigurationDeleteDialogComponent,
  ],
})
export class SanctionConfigurationModule {}
