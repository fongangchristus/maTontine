import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TontineComponent } from './list/tontine.component';
import { TontineDetailComponent } from './detail/tontine-detail.component';
import { TontineUpdateComponent } from './update/tontine-update.component';
import { TontineDeleteDialogComponent } from './delete/tontine-delete-dialog.component';
import { TontineRoutingModule } from './route/tontine-routing.module';

@NgModule({
  imports: [SharedModule, TontineRoutingModule],
  declarations: [TontineComponent, TontineDetailComponent, TontineUpdateComponent, TontineDeleteDialogComponent],
})
export class TontineModule {}
