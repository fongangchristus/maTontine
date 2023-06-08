import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MonnaieComponent } from './list/monnaie.component';
import { MonnaieDetailComponent } from './detail/monnaie-detail.component';
import { MonnaieUpdateComponent } from './update/monnaie-update.component';
import { MonnaieDeleteDialogComponent } from './delete/monnaie-delete-dialog.component';
import { MonnaieRoutingModule } from './route/monnaie-routing.module';

@NgModule({
  imports: [SharedModule, MonnaieRoutingModule],
  declarations: [MonnaieComponent, MonnaieDetailComponent, MonnaieUpdateComponent, MonnaieDeleteDialogComponent],
})
export class MonnaieModule {}
