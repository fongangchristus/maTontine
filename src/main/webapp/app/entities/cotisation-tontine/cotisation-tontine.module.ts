import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CotisationTontineComponent } from './list/cotisation-tontine.component';
import { CotisationTontineDetailComponent } from './detail/cotisation-tontine-detail.component';
import { CotisationTontineUpdateComponent } from './update/cotisation-tontine-update.component';
import { CotisationTontineDeleteDialogComponent } from './delete/cotisation-tontine-delete-dialog.component';
import { CotisationTontineRoutingModule } from './route/cotisation-tontine-routing.module';

@NgModule({
  imports: [SharedModule, CotisationTontineRoutingModule],
  declarations: [
    CotisationTontineComponent,
    CotisationTontineDetailComponent,
    CotisationTontineUpdateComponent,
    CotisationTontineDeleteDialogComponent,
  ],
})
export class CotisationTontineModule {}
