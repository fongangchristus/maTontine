import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaiementTontineComponent } from './list/paiement-tontine.component';
import { PaiementTontineDetailComponent } from './detail/paiement-tontine-detail.component';
import { PaiementTontineUpdateComponent } from './update/paiement-tontine-update.component';
import { PaiementTontineDeleteDialogComponent } from './delete/paiement-tontine-delete-dialog.component';
import { PaiementTontineRoutingModule } from './route/paiement-tontine-routing.module';

@NgModule({
  imports: [SharedModule, PaiementTontineRoutingModule],
  declarations: [
    PaiementTontineComponent,
    PaiementTontineDetailComponent,
    PaiementTontineUpdateComponent,
    PaiementTontineDeleteDialogComponent,
  ],
})
export class PaiementTontineModule {}
