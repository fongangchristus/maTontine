import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFonctionAdherent } from '../fonction-adherent.model';
import { FonctionAdherentService } from '../service/fonction-adherent.service';

@Injectable({ providedIn: 'root' })
export class FonctionAdherentRoutingResolveService implements Resolve<IFonctionAdherent | null> {
  constructor(protected service: FonctionAdherentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFonctionAdherent | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fonctionAdherent: HttpResponse<IFonctionAdherent>) => {
          if (fonctionAdherent.body) {
            return of(fonctionAdherent.body);
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
