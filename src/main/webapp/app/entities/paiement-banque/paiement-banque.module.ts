import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaiementBanqueComponent } from './list/paiement-banque.component';
import { PaiementBanqueDetailComponent } from './detail/paiement-banque-detail.component';
import { PaiementBanqueUpdateComponent } from './update/paiement-banque-update.component';
import { PaiementBanqueDeleteDialogComponent } from './delete/paiement-banque-delete-dialog.component';
import { PaiementBanqueRoutingModule } from './route/paiement-banque-routing.module';

@NgModule({
  imports: [SharedModule, PaiementBanqueRoutingModule],
  declarations: [
    PaiementBanqueComponent,
    PaiementBanqueDetailComponent,
    PaiementBanqueUpdateComponent,
    PaiementBanqueDeleteDialogComponent,
  ],
})
export class PaiementBanqueModule {}
