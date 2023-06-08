import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DecaissementTontineComponent } from './list/decaissement-tontine.component';
import { DecaissementTontineDetailComponent } from './detail/decaissement-tontine-detail.component';
import { DecaissementTontineUpdateComponent } from './update/decaissement-tontine-update.component';
import { DecaissementTontineDeleteDialogComponent } from './delete/decaissement-tontine-delete-dialog.component';
import { DecaissementTontineRoutingModule } from './route/decaissement-tontine-routing.module';

@NgModule({
  imports: [SharedModule, DecaissementTontineRoutingModule],
  declarations: [
    DecaissementTontineComponent,
    DecaissementTontineDetailComponent,
    DecaissementTontineUpdateComponent,
    DecaissementTontineDeleteDialogComponent,
  ],
})
export class DecaissementTontineModule {}
