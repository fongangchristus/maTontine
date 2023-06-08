import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompteRIBComponent } from './list/compte-rib.component';
import { CompteRIBDetailComponent } from './detail/compte-rib-detail.component';
import { CompteRIBUpdateComponent } from './update/compte-rib-update.component';
import { CompteRIBDeleteDialogComponent } from './delete/compte-rib-delete-dialog.component';
import { CompteRIBRoutingModule } from './route/compte-rib-routing.module';

@NgModule({
  imports: [SharedModule, CompteRIBRoutingModule],
  declarations: [CompteRIBComponent, CompteRIBDetailComponent, CompteRIBUpdateComponent, CompteRIBDeleteDialogComponent],
})
export class CompteRIBModule {}
