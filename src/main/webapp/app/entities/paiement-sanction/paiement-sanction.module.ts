import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaiementSanctionComponent } from './list/paiement-sanction.component';
import { PaiementSanctionDetailComponent } from './detail/paiement-sanction-detail.component';
import { PaiementSanctionUpdateComponent } from './update/paiement-sanction-update.component';
import { PaiementSanctionDeleteDialogComponent } from './delete/paiement-sanction-delete-dialog.component';
import { PaiementSanctionRoutingModule } from './route/paiement-sanction-routing.module';

@NgModule({
  imports: [SharedModule, PaiementSanctionRoutingModule],
  declarations: [
    PaiementSanctionComponent,
    PaiementSanctionDetailComponent,
    PaiementSanctionUpdateComponent,
    PaiementSanctionDeleteDialogComponent,
  ],
})
export class PaiementSanctionModule {}
