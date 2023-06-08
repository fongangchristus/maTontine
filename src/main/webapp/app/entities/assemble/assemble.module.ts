import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssembleComponent } from './list/assemble.component';
import { AssembleDetailComponent } from './detail/assemble-detail.component';
import { AssembleUpdateComponent } from './update/assemble-update.component';
import { AssembleDeleteDialogComponent } from './delete/assemble-delete-dialog.component';
import { AssembleRoutingModule } from './route/assemble-routing.module';

@NgModule({
  imports: [SharedModule, AssembleRoutingModule],
  declarations: [AssembleComponent, AssembleDetailComponent, AssembleUpdateComponent, AssembleDeleteDialogComponent],
})
export class AssembleModule {}
