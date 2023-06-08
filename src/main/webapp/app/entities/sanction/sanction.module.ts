import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SanctionComponent } from './list/sanction.component';
import { SanctionDetailComponent } from './detail/sanction-detail.component';
import { SanctionUpdateComponent } from './update/sanction-update.component';
import { SanctionDeleteDialogComponent } from './delete/sanction-delete-dialog.component';
import { SanctionRoutingModule } from './route/sanction-routing.module';

@NgModule({
  imports: [SharedModule, SanctionRoutingModule],
  declarations: [SanctionComponent, SanctionDetailComponent, SanctionUpdateComponent, SanctionDeleteDialogComponent],
})
export class SanctionModule {}
