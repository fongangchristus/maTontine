import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompteBanque } from '../compte-banque.model';
import { CompteBanqueService } from '../service/compte-banque.service';

@Injectable({ providedIn: 'root' })
export class CompteBanqueRoutingResolveService implements Resolve<ICompteBanque | null> {
  constructor(protected service: CompteBanqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompteBanque | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compteBanque: HttpResponse<ICompteBanque>) => {
          if (compteBanque.body) {
            return of(compteBanque.body);
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
