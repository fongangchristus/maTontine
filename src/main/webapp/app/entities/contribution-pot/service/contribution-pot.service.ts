import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContributionPot, NewContributionPot } from '../contribution-pot.model';

export type PartialUpdateContributionPot = Partial<IContributionPot> & Pick<IContributionPot, 'id'>;

type RestOf<T extends IContributionPot | NewContributionPot> = Omit<T, 'dateContribution'> & {
  dateContribution?: string | null;
};

export type RestContributionPot = RestOf<IContributionPot>;

export type NewRestContributionPot = RestOf<NewContributionPot>;

export type PartialUpdateRestContributionPot = RestOf<PartialUpdateContributionPot>;

export type EntityResponseType = HttpResponse<IContributionPot>;
export type EntityArrayResponseType = HttpResponse<IContributionPot[]>;

@Injectable({ providedIn: 'root' })
export class ContributionPotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contribution-pots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contributionPot: NewContributionPot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contributionPot);
    return this.http
      .post<RestContributionPot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contributionPot: IContributionPot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contributionPot);
    return this.http
      .put<RestContributionPot>(`${this.resourceUrl}/${this.getContributionPotIdentifier(contributionPot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contributionPot: PartialUpdateContributionPot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contributionPot);
    return this.http
      .patch<RestContributionPot>(`${this.resourceUrl}/${this.getContributionPotIdentifier(contributionPot)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContributionPot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContributionPot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContributionPotIdentifier(contributionPot: Pick<IContributionPot, 'id'>): number {
    return contributionPot.id;
  }

  compareContributionPot(o1: Pick<IContributionPot, 'id'> | null, o2: Pick<IContributionPot, 'id'> | null): boolean {
    return o1 && o2 ? this.getContributionPotIdentifier(o1) === this.getContributionPotIdentifier(o2) : o1 === o2;
  }

  addContributionPotToCollectionIfMissing<Type extends Pick<IContributionPot, 'id'>>(
    contributionPotCollection: Type[],
    ...contributionPotsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contributionPots: Type[] = contributionPotsToCheck.filter(isPresent);
    if (contributionPots.length > 0) {
      const contributionPotCollectionIdentifiers = contributionPotCollection.map(
        contributionPotItem => this.getContributionPotIdentifier(contributionPotItem)!
      );
      const contributionPotsToAdd = contributionPots.filter(contributionPotItem => {
        const contributionPotIdentifier = this.getContributionPotIdentifier(contributionPotItem);
        if (contributionPotCollectionIdentifiers.includes(contributionPotIdentifier)) {
          return false;
        }
        contributionPotCollectionIdentifiers.push(contributionPotIdentifier);
        return true;
      });
      return [...contributionPotsToAdd, ...contributionPotCollection];
    }
    return contributionPotCollection;
  }

  protected convertDateFromClient<T extends IContributionPot | NewContributionPot | PartialUpdateContributionPot>(
    contributionPot: T
  ): RestOf<T> {
    return {
      ...contributionPot,
      dateContribution: contributionPot.dateContribution?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContributionPot: RestContributionPot): IContributionPot {
    return {
      ...restContributionPot,
      dateContribution: restContributionPot.dateContribution ? dayjs(restContributionPot.dateContribution) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContributionPot>): HttpResponse<IContributionPot> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContributionPot[]>): HttpResponse<IContributionPot[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
