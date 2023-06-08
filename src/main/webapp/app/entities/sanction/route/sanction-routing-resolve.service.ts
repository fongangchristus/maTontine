import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISanction } from '../sanction.model';
import { SanctionService } from '../service/sanction.service';

@Injectable({ providedIn: 'root' })
export class SanctionRoutingResolveService implements Resolve<ISanction | null> {
  constructor(protected service: SanctionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISanction | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sanction: HttpResponse<ISanction>) => {
          if (sanction.body) {
            return of(sanction.body);
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
