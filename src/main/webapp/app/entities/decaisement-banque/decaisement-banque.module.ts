import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DecaisementBanqueComponent } from './list/decaisement-banque.component';
import { DecaisementBanqueDetailComponent } from './detail/decaisement-banque-detail.component';
import { DecaisementBanqueUpdateComponent } from './update/decaisement-banque-update.component';
import { DecaisementBanqueDeleteDialogComponent } from './delete/decaisement-banque-delete-dialog.component';
import { DecaisementBanqueRoutingModule } from './route/decaisement-banque-routing.module';

@NgModule({
  imports: [SharedModule, DecaisementBanqueRoutingModule],
  declarations: [
    DecaisementBanqueComponent,
    DecaisementBanqueDetailComponent,
    DecaisementBanqueUpdateComponent,
    DecaisementBanqueDeleteDialogComponent,
  ],
})
export class DecaisementBanqueModule {}
