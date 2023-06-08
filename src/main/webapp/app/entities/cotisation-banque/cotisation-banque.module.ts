import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CotisationBanqueComponent } from './list/cotisation-banque.component';
import { CotisationBanqueDetailComponent } from './detail/cotisation-banque-detail.component';
import { CotisationBanqueUpdateComponent } from './update/cotisation-banque-update.component';
import { CotisationBanqueDeleteDialogComponent } from './delete/cotisation-banque-delete-dialog.component';
import { CotisationBanqueRoutingModule } from './route/cotisation-banque-routing.module';

@NgModule({
  imports: [SharedModule, CotisationBanqueRoutingModule],
  declarations: [
    CotisationBanqueComponent,
    CotisationBanqueDetailComponent,
    CotisationBanqueUpdateComponent,
    CotisationBanqueDeleteDialogComponent,
  ],
})
export class CotisationBanqueModule {}
