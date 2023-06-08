import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GestionnaireBanqueComponent } from './list/gestionnaire-banque.component';
import { GestionnaireBanqueDetailComponent } from './detail/gestionnaire-banque-detail.component';
import { GestionnaireBanqueUpdateComponent } from './update/gestionnaire-banque-update.component';
import { GestionnaireBanqueDeleteDialogComponent } from './delete/gestionnaire-banque-delete-dialog.component';
import { GestionnaireBanqueRoutingModule } from './route/gestionnaire-banque-routing.module';

@NgModule({
  imports: [SharedModule, GestionnaireBanqueRoutingModule],
  declarations: [
    GestionnaireBanqueComponent,
    GestionnaireBanqueDetailComponent,
    GestionnaireBanqueUpdateComponent,
    GestionnaireBanqueDeleteDialogComponent,
  ],
})
export class GestionnaireBanqueModule {}
