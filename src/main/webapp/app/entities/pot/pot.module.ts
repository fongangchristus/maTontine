import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PotComponent } from './list/pot.component';
import { PotDetailComponent } from './detail/pot-detail.component';
import { PotUpdateComponent } from './update/pot-update.component';
import { PotDeleteDialogComponent } from './delete/pot-delete-dialog.component';
import { PotRoutingModule } from './route/pot-routing.module';

@NgModule({
  imports: [SharedModule, PotRoutingModule],
  declarations: [PotComponent, PotDetailComponent, PotUpdateComponent, PotDeleteDialogComponent],
})
export class PotModule {}
