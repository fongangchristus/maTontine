import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiementSanction, NewPaiementSanction } from '../paiement-sanction.model';

export type PartialUpdatePaiementSanction = Partial<IPaiementSanction> & Pick<IPaiementSanction, 'id'>;

export type EntityResponseType = HttpResponse<IPaiementSanction>;
export type EntityArrayResponseType = HttpResponse<IPaiementSanction[]>;

@Injectable({ providedIn: 'root' })
export class PaiementSanctionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-sanctions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiementSanction: NewPaiementSanction): Observable<EntityResponseType> {
    return this.http.post<IPaiementSanction>(this.resourceUrl, paiementSanction, { observe: 'response' });
  }

  update(paiementSanction: IPaiementSanction): Observable<EntityResponseType> {
    return this.http.put<IPaiementSanction>(
      `${this.resourceUrl}/${this.getPaiementSanctionIdentifier(paiementSanction)}`,
      paiementSanction,
      { observe: 'response' }
    );
  }

  partialUpdate(paiementSanction: PartialUpdatePaiementSanction): Observable<EntityResponseType> {
    return this.http.patch<IPaiementSanction>(
      `${this.resourceUrl}/${this.getPaiementSanctionIdentifier(paiementSanction)}`,
      paiementSanction,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaiementSanction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaiementSanction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaiementSanctionIdentifier(paiementSanction: Pick<IPaiementSanction, 'id'>): number {
    return paiementSanction.id;
  }

  comparePaiementSanction(o1: Pick<IPaiementSanction, 'id'> | null, o2: Pick<IPaiementSanction, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaiementSanctionIdentifier(o1) === this.getPaiementSanctionIdentifier(o2) : o1 === o2;
  }

  addPaiementSanctionToCollectionIfMissing<Type extends Pick<IPaiementSanction, 'id'>>(
    paiementSanctionCollection: Type[],
    ...paiementSanctionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementSanctions: Type[] = paiementSanctionsToCheck.filter(isPresent);
    if (paiementSanctions.length > 0) {
      const paiementSanctionCollectionIdentifiers = paiementSanctionCollection.map(
        paiementSanctionItem => this.getPaiementSanctionIdentifier(paiementSanctionItem)!
      );
      const paiementSanctionsToAdd = paiementSanctions.filter(paiementSanctionItem => {
        const paiementSanctionIdentifier = this.getPaiementSanctionIdentifier(paiementSanctionItem);
        if (paiementSanctionCollectionIdentifiers.includes(paiementSanctionIdentifier)) {
          return false;
        }
        paiementSanctionCollectionIdentifiers.push(paiementSanctionIdentifier);
        return true;
      });
      return [...paiementSanctionsToAdd, ...paiementSanctionCollection];
    }
    return paiementSanctionCollection;
  }
}
