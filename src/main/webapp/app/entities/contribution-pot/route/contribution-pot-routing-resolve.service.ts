import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContributionPot } from '../contribution-pot.model';
import { ContributionPotService } from '../service/contribution-pot.service';

@Injectable({ providedIn: 'root' })
export class ContributionPotRoutingResolveService implements Resolve<IContributionPot | null> {
  constructor(protected service: ContributionPotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContributionPot | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contributionPot: HttpResponse<IContributionPot>) => {
          if (contributionPot.body) {
            return of(contributionPot.body);
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
