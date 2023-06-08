import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiementAdhesion, NewPaiementAdhesion } from '../paiement-adhesion.model';

export type PartialUpdatePaiementAdhesion = Partial<IPaiementAdhesion> & Pick<IPaiementAdhesion, 'id'>;

export type EntityResponseType = HttpResponse<IPaiementAdhesion>;
export type EntityArrayResponseType = HttpResponse<IPaiementAdhesion[]>;

@Injectable({ providedIn: 'root' })
export class PaiementAdhesionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-adhesions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiementAdhesion: NewPaiementAdhesion): Observable<EntityResponseType> {
    return this.http.post<IPaiementAdhesion>(this.resourceUrl, paiementAdhesion, { observe: 'response' });
  }

  update(paiementAdhesion: IPaiementAdhesion): Observable<EntityResponseType> {
    return this.http.put<IPaiementAdhesion>(
      `${this.resourceUrl}/${this.getPaiementAdhesionIdentifier(paiementAdhesion)}`,
      paiementAdhesion,
      { observe: 'response' }
    );
  }

  partialUpdate(paiementAdhesion: PartialUpdatePaiementAdhesion): Observable<EntityResponseType> {
    return this.http.patch<IPaiementAdhesion>(
      `${this.resourceUrl}/${this.getPaiementAdhesionIdentifier(paiementAdhesion)}`,
      paiementAdhesion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaiementAdhesion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaiementAdhesion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaiementAdhesionIdentifier(paiementAdhesion: Pick<IPaiementAdhesion, 'id'>): number {
    return paiementAdhesion.id;
  }

  comparePaiementAdhesion(o1: Pick<IPaiementAdhesion, 'id'> | null, o2: Pick<IPaiementAdhesion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaiementAdhesionIdentifier(o1) === this.getPaiementAdhesionIdentifier(o2) : o1 === o2;
  }

  addPaiementAdhesionToCollectionIfMissing<Type extends Pick<IPaiementAdhesion, 'id'>>(
    paiementAdhesionCollection: Type[],
    ...paiementAdhesionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementAdhesions: Type[] = paiementAdhesionsToCheck.filter(isPresent);
    if (paiementAdhesions.length > 0) {
      const paiementAdhesionCollectionIdentifiers = paiementAdhesionCollection.map(
        paiementAdhesionItem => this.getPaiementAdhesionIdentifier(paiementAdhesionItem)!
      );
      const paiementAdhesionsToAdd = paiementAdhesions.filter(paiementAdhesionItem => {
        const paiementAdhesionIdentifier = this.getPaiementAdhesionIdentifier(paiementAdhesionItem);
        if (paiementAdhesionCollectionIdentifiers.includes(paiementAdhesionIdentifier)) {
          return false;
        }
        paiementAdhesionCollectionIdentifiers.push(paiementAdhesionIdentifier);
        return true;
      });
      return [...paiementAdhesionsToAdd, ...paiementAdhesionCollection];
    }
    return paiementAdhesionCollection;
  }
}
