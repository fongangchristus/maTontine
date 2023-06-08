import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPot, NewPot } from '../pot.model';

export type PartialUpdatePot = Partial<IPot> & Pick<IPot, 'id'>;

type RestOf<T extends IPot | NewPot> = Omit<T, 'dateDebutCollecte' | 'dateFinCollecte'> & {
  dateDebutCollecte?: string | null;
  dateFinCollecte?: string | null;
};

export type RestPot = RestOf<IPot>;

export type NewRestPot = RestOf<NewPot>;

export type PartialUpdateRestPot = RestOf<PartialUpdatePot>;

export type EntityResponseType = HttpResponse<IPot>;
export type EntityArrayResponseType = HttpResponse<IPot[]>;

@Injectable({ providedIn: 'root' })
export class PotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pot: NewPot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pot);
    return this.http.post<RestPot>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pot: IPot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pot);
    return this.http
      .put<RestPot>(`${this.resourceUrl}/${this.getPotIdentifier(pot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pot: PartialUpdatePot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pot);
    return this.http
      .patch<RestPot>(`${this.resourceUrl}/${this.getPotIdentifier(pot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPotIdentifier(pot: Pick<IPot, 'id'>): number {
    return pot.id;
  }

  comparePot(o1: Pick<IPot, 'id'> | null, o2: Pick<IPot, 'id'> | null): boolean {
    return o1 && o2 ? this.getPotIdentifier(o1) === this.getPotIdentifier(o2) : o1 === o2;
  }

  addPotToCollectionIfMissing<Type extends Pick<IPot, 'id'>>(potCollection: Type[], ...potsToCheck: (Type | null | undefined)[]): Type[] {
    const pots: Type[] = potsToCheck.filter(isPresent);
    if (pots.length > 0) {
      const potCollectionIdentifiers = potCollection.map(potItem => this.getPotIdentifier(potItem)!);
      const potsToAdd = pots.filter(potItem => {
        const potIdentifier = this.getPotIdentifier(potItem);
        if (potCollectionIdentifiers.includes(potIdentifier)) {
          return false;
        }
        potCollectionIdentifiers.push(potIdentifier);
        return true;
      });
      return [...potsToAdd, ...potCollection];
    }
    return potCollection;
  }

  protected convertDateFromClient<T extends IPot | NewPot | PartialUpdatePot>(pot: T): RestOf<T> {
    return {
      ...pot,
      dateDebutCollecte: pot.dateDebutCollecte?.format(DATE_FORMAT) ?? null,
      dateFinCollecte: pot.dateFinCollecte?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPot: RestPot): IPot {
    return {
      ...restPot,
      dateDebutCollecte: restPot.dateDebutCollecte ? dayjs(restPot.dateDebutCollecte) : undefined,
      dateFinCollecte: restPot.dateFinCollecte ? dayjs(restPot.dateFinCollecte) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPot>): HttpResponse<IPot> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPot[]>): HttpResponse<IPot[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
