import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeEvenement, NewTypeEvenement } from '../type-evenement.model';

export type PartialUpdateTypeEvenement = Partial<ITypeEvenement> & Pick<ITypeEvenement, 'id'>;

export type EntityResponseType = HttpResponse<ITypeEvenement>;
export type EntityArrayResponseType = HttpResponse<ITypeEvenement[]>;

@Injectable({ providedIn: 'root' })
export class TypeEvenementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-evenements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeEvenement: NewTypeEvenement): Observable<EntityResponseType> {
    return this.http.post<ITypeEvenement>(this.resourceUrl, typeEvenement, { observe: 'response' });
  }

  update(typeEvenement: ITypeEvenement): Observable<EntityResponseType> {
    return this.http.put<ITypeEvenement>(`${this.resourceUrl}/${this.getTypeEvenementIdentifier(typeEvenement)}`, typeEvenement, {
      observe: 'response',
    });
  }

  partialUpdate(typeEvenement: PartialUpdateTypeEvenement): Observable<EntityResponseType> {
    return this.http.patch<ITypeEvenement>(`${this.resourceUrl}/${this.getTypeEvenementIdentifier(typeEvenement)}`, typeEvenement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeEvenement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeEvenementIdentifier(typeEvenement: Pick<ITypeEvenement, 'id'>): number {
    return typeEvenement.id;
  }

  compareTypeEvenement(o1: Pick<ITypeEvenement, 'id'> | null, o2: Pick<ITypeEvenement, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeEvenementIdentifier(o1) === this.getTypeEvenementIdentifier(o2) : o1 === o2;
  }

  addTypeEvenementToCollectionIfMissing<Type extends Pick<ITypeEvenement, 'id'>>(
    typeEvenementCollection: Type[],
    ...typeEvenementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeEvenements: Type[] = typeEvenementsToCheck.filter(isPresent);
    if (typeEvenements.length > 0) {
      const typeEvenementCollectionIdentifiers = typeEvenementCollection.map(
        typeEvenementItem => this.getTypeEvenementIdentifier(typeEvenementItem)!
      );
      const typeEvenementsToAdd = typeEvenements.filter(typeEvenementItem => {
        const typeEvenementIdentifier = this.getTypeEvenementIdentifier(typeEvenementItem);
        if (typeEvenementCollectionIdentifiers.includes(typeEvenementIdentifier)) {
          return false;
        }
        typeEvenementCollectionIdentifiers.push(typeEvenementIdentifier);
        return true;
      });
      return [...typeEvenementsToAdd, ...typeEvenementCollection];
    }
    return typeEvenementCollection;
  }
}
