import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypePotComponent } from './list/type-pot.component';
import { TypePotDetailComponent } from './detail/type-pot-detail.component';
import { TypePotUpdateComponent } from './update/type-pot-update.component';
import { TypePotDeleteDialogComponent } from './delete/type-pot-delete-dialog.component';
import { TypePotRoutingModule } from './route/type-pot-routing.module';

@NgModule({
  imports: [SharedModule, TypePotRoutingModule],
  declarations: [TypePotComponent, TypePotDetailComponent, TypePotUpdateComponent, TypePotDeleteDialogComponent],
})
export class TypePotModule {}
