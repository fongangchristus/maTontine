import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompteBanqueComponent } from './list/compte-banque.component';
import { CompteBanqueDetailComponent } from './detail/compte-banque-detail.component';
import { CompteBanqueUpdateComponent } from './update/compte-banque-update.component';
import { CompteBanqueDeleteDialogComponent } from './delete/compte-banque-delete-dialog.component';
import { CompteBanqueRoutingModule } from './route/compte-banque-routing.module';

@NgModule({
  imports: [SharedModule, CompteBanqueRoutingModule],
  declarations: [CompteBanqueComponent, CompteBanqueDetailComponent, CompteBanqueUpdateComponent, CompteBanqueDeleteDialogComponent],
})
export class CompteBanqueModule {}
