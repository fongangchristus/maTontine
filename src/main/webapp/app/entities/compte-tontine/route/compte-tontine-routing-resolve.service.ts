import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompteTontine } from '../compte-tontine.model';
import { CompteTontineService } from '../service/compte-tontine.service';

@Injectable({ providedIn: 'root' })
export class CompteTontineRoutingResolveService implements Resolve<ICompteTontine | null> {
  constructor(protected service: CompteTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompteTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compteTontine: HttpResponse<ICompteTontine>) => {
          if (compteTontine.body) {
            return of(compteTontine.body);
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
