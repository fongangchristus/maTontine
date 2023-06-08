import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionTontine } from '../session-tontine.model';
import { SessionTontineService } from '../service/session-tontine.service';

@Injectable({ providedIn: 'root' })
export class SessionTontineRoutingResolveService implements Resolve<ISessionTontine | null> {
  constructor(protected service: SessionTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISessionTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sessionTontine: HttpResponse<ISessionTontine>) => {
          if (sessionTontine.body) {
            return of(sessionTontine.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
