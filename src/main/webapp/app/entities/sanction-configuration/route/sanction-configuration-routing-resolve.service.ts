import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISanctionConfiguration } from '../sanction-configuration.model';
import { SanctionConfigurationService } from '../service/sanction-configuration.service';

@Injectable({ providedIn: 'root' })
export class SanctionConfigurationRoutingResolveService implements Resolve<ISanctionConfiguration | null> {
  constructor(protected service: SanctionConfigurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISanctionConfiguration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sanctionConfiguration: HttpResponse<ISanctionConfiguration>) => {
          if (sanctionConfiguration.body) {
            return of(sanctionConfiguration.body);
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
