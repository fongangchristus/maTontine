import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoriquePersonne } from '../historique-personne.model';
import { HistoriquePersonneService } from '../service/historique-personne.service';

@Injectable({ providedIn: 'root' })
export class HistoriquePersonneRoutingResolveService implements Resolve<IHistoriquePersonne | null> {
  constructor(protected service: HistoriquePersonneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHistoriquePersonne | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((historiquePersonne: HttpResponse<IHistoriquePersonne>) => {
          if (historiquePersonne.body) {
            return of(historiquePersonne.body);
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
