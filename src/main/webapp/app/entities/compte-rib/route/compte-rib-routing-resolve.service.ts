import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompteRIB } from '../compte-rib.model';
import { CompteRIBService } from '../service/compte-rib.service';

@Injectable({ providedIn: 'root' })
export class CompteRIBRoutingResolveService implements Resolve<ICompteRIB | null> {
  constructor(protected service: CompteRIBService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompteRIB | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compteRIB: HttpResponse<ICompteRIB>) => {
          if (compteRIB.body) {
            return of(compteRIB.body);
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
