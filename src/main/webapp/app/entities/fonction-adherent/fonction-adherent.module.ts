import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FonctionAdherentComponent } from './list/fonction-adherent.component';
import { FonctionAdherentDetailComponent } from './detail/fonction-adherent-detail.component';
import { FonctionAdherentUpdateComponent } from './update/fonction-adherent-update.component';
import { FonctionAdherentDeleteDialogComponent } from './delete/fonction-adherent-delete-dialog.component';
import { FonctionAdherentRoutingModule } from './route/fonction-adherent-routing.module';

@NgModule({
  imports: [SharedModule, FonctionAdherentRoutingModule],
  declarations: [
    FonctionAdherentComponent,
    FonctionAdherentDetailComponent,
    FonctionAdherentUpdateComponent,
    FonctionAdherentDeleteDialogComponent,
  ],
})
export class FonctionAdherentModule {}
