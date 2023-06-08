import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HistoriquePersonneComponent } from './list/historique-personne.component';
import { HistoriquePersonneDetailComponent } from './detail/historique-personne-detail.component';
import { HistoriquePersonneUpdateComponent } from './update/historique-personne-update.component';
import { HistoriquePersonneDeleteDialogComponent } from './delete/historique-personne-delete-dialog.component';
import { HistoriquePersonneRoutingModule } from './route/historique-personne-routing.module';

@NgModule({
  imports: [SharedModule, HistoriquePersonneRoutingModule],
  declarations: [
    HistoriquePersonneComponent,
    HistoriquePersonneDetailComponent,
    HistoriquePersonneUpdateComponent,
    HistoriquePersonneDeleteDialogComponent,
  ],
})
export class HistoriquePersonneModule {}
