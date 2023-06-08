import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GestionnaireTontineComponent } from './list/gestionnaire-tontine.component';
import { GestionnaireTontineDetailComponent } from './detail/gestionnaire-tontine-detail.component';
import { GestionnaireTontineUpdateComponent } from './update/gestionnaire-tontine-update.component';
import { GestionnaireTontineDeleteDialogComponent } from './delete/gestionnaire-tontine-delete-dialog.component';
import { GestionnaireTontineRoutingModule } from './route/gestionnaire-tontine-routing.module';

@NgModule({
  imports: [SharedModule, GestionnaireTontineRoutingModule],
  declarations: [
    GestionnaireTontineComponent,
    GestionnaireTontineDetailComponent,
    GestionnaireTontineUpdateComponent,
    GestionnaireTontineDeleteDialogComponent,
  ],
})
export class GestionnaireTontineModule {}
