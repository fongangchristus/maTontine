import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormuleAdhesionComponent } from './list/formule-adhesion.component';
import { FormuleAdhesionDetailComponent } from './detail/formule-adhesion-detail.component';
import { FormuleAdhesionUpdateComponent } from './update/formule-adhesion-update.component';
import { FormuleAdhesionDeleteDialogComponent } from './delete/formule-adhesion-delete-dialog.component';
import { FormuleAdhesionRoutingModule } from './route/formule-adhesion-routing.module';

@NgModule({
  imports: [SharedModule, FormuleAdhesionRoutingModule],
  declarations: [
    FormuleAdhesionComponent,
    FormuleAdhesionDetailComponent,
    FormuleAdhesionUpdateComponent,
    FormuleAdhesionDeleteDialogComponent,
  ],
})
export class FormuleAdhesionModule {}
