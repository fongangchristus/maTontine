import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompteBanque, NewCompteBanque } from '../compte-banque.model';

export type PartialUpdateCompteBanque = Partial<ICompteBanque> & Pick<ICompteBanque, 'id'>;

export type EntityResponseType = HttpResponse<ICompteBanque>;
export type EntityArrayResponseType = HttpResponse<ICompteBanque[]>;

@Injectable({ providedIn: 'root' })
export class CompteBanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compte-banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compteBanque: NewCompteBanque): Observable<EntityResponseType> {
    return this.http.post<ICompteBanque>(this.resourceUrl, compteBanque, { observe: 'response' });
  }

  update(compteBanque: ICompteBanque): Observable<EntityResponseType> {
    return this.http.put<ICompteBanque>(`${this.resourceUrl}/${this.getCompteBanqueIdentifier(compteBanque)}`, compteBanque, {
      observe: 'response',
    });
  }

  partialUpdate(compteBanque: PartialUpdateCompteBanque): Observable<EntityResponseType> {
    return this.http.patch<ICompteBanque>(`${this.resourceUrl}/${this.getCompteBanqueIdentifier(compteBanque)}`, compteBanque, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompteBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompteBanque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteBanqueIdentifier(compteBanque: Pick<ICompteBanque, 'id'>): number {
    return compteBanque.id;
  }

  compareCompteBanque(o1: Pick<ICompteBanque, 'id'> | null, o2: Pick<ICompteBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteBanqueIdentifier(o1) === this.getCompteBanqueIdentifier(o2) : o1 === o2;
  }

  addCompteBanqueToCollectionIfMissing<Type extends Pick<ICompteBanque, 'id'>>(
    compteBanqueCollection: Type[],
    ...compteBanquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compteBanques: Type[] = compteBanquesToCheck.filter(isPresent);
    if (compteBanques.length > 0) {
      const compteBanqueCollectionIdentifiers = compteBanqueCollection.map(
        compteBanqueItem => this.getCompteBanqueIdentifier(compteBanqueItem)!
      );
      const compteBanquesToAdd = compteBanques.filter(compteBanqueItem => {
        const compteBanqueIdentifier = this.getCompteBanqueIdentifier(compteBanqueItem);
        if (compteBanqueCollectionIdentifiers.includes(compteBanqueIdentifier)) {
          return false;
        }
        compteBanqueCollectionIdentifiers.push(compteBanqueIdentifier);
        return true;
      });
      return [...compteBanquesToAdd, ...compteBanqueCollection];
    }
    return compteBanqueCollection;
  }
}
