import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompteTontineComponent } from './list/compte-tontine.component';
import { CompteTontineDetailComponent } from './detail/compte-tontine-detail.component';
import { CompteTontineUpdateComponent } from './update/compte-tontine-update.component';
import { CompteTontineDeleteDialogComponent } from './delete/compte-tontine-delete-dialog.component';
import { CompteTontineRoutingModule } from './route/compte-tontine-routing.module';

@NgModule({
  imports: [SharedModule, CompteTontineRoutingModule],
  declarations: [CompteTontineComponent, CompteTontineDetailComponent, CompteTontineUpdateComponent, CompteTontineDeleteDialogComponent],
})
export class CompteTontineModule {}
