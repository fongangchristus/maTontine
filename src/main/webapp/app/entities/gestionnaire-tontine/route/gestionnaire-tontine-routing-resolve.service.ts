import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { GestionnaireTontineService } from '../service/gestionnaire-tontine.service';

@Injectable({ providedIn: 'root' })
export class GestionnaireTontineRoutingResolveService implements Resolve<IGestionnaireTontine | null> {
  constructor(protected service: GestionnaireTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGestionnaireTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gestionnaireTontine: HttpResponse<IGestionnaireTontine>) => {
          if (gestionnaireTontine.body) {
            return of(gestionnaireTontine.body);
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
