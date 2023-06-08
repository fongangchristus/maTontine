import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SessionTontineComponent } from './list/session-tontine.component';
import { SessionTontineDetailComponent } from './detail/session-tontine-detail.component';
import { SessionTontineUpdateComponent } from './update/session-tontine-update.component';
import { SessionTontineDeleteDialogComponent } from './delete/session-tontine-delete-dialog.component';
import { SessionTontineRoutingModule } from './route/session-tontine-routing.module';

@NgModule({
  imports: [SharedModule, SessionTontineRoutingModule],
  declarations: [
    SessionTontineComponent,
    SessionTontineDetailComponent,
    SessionTontineUpdateComponent,
    SessionTontineDeleteDialogComponent,
  ],
})
export class SessionTontineModule {}
