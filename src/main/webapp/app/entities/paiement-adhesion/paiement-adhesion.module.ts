import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaiementAdhesionComponent } from './list/paiement-adhesion.component';
import { PaiementAdhesionDetailComponent } from './detail/paiement-adhesion-detail.component';
import { PaiementAdhesionUpdateComponent } from './update/paiement-adhesion-update.component';
import { PaiementAdhesionDeleteDialogComponent } from './delete/paiement-adhesion-delete-dialog.component';
import { PaiementAdhesionRoutingModule } from './route/paiement-adhesion-routing.module';

@NgModule({
  imports: [SharedModule, PaiementAdhesionRoutingModule],
  declarations: [
    PaiementAdhesionComponent,
    PaiementAdhesionDetailComponent,
    PaiementAdhesionUpdateComponent,
    PaiementAdhesionDeleteDialogComponent,
  ],
})
export class PaiementAdhesionModule {}
