import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFonction, NewFonction } from '../fonction.model';

export type PartialUpdateFonction = Partial<IFonction> & Pick<IFonction, 'id'>;

export type EntityResponseType = HttpResponse<IFonction>;
export type EntityArrayResponseType = HttpResponse<IFonction[]>;

@Injectable({ providedIn: 'root' })
export class FonctionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fonctions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fonction: NewFonction): Observable<EntityResponseType> {
    return this.http.post<IFonction>(this.resourceUrl, fonction, { observe: 'response' });
  }

  update(fonction: IFonction): Observable<EntityResponseType> {
    return this.http.put<IFonction>(`${this.resourceUrl}/${this.getFonctionIdentifier(fonction)}`, fonction, { observe: 'response' });
  }

  partialUpdate(fonction: PartialUpdateFonction): Observable<EntityResponseType> {
    return this.http.patch<IFonction>(`${this.resourceUrl}/${this.getFonctionIdentifier(fonction)}`, fonction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFonction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFonction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFonctionIdentifier(fonction: Pick<IFonction, 'id'>): number {
    return fonction.id;
  }

  compareFonction(o1: Pick<IFonction, 'id'> | null, o2: Pick<IFonction, 'id'> | null): boolean {
    return o1 && o2 ? this.getFonctionIdentifier(o1) === this.getFonctionIdentifier(o2) : o1 === o2;
  }

  addFonctionToCollectionIfMissing<Type extends Pick<IFonction, 'id'>>(
    fonctionCollection: Type[],
    ...fonctionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fonctions: Type[] = fonctionsToCheck.filter(isPresent);
    if (fonctions.length > 0) {
      const fonctionCollectionIdentifiers = fonctionCollection.map(fonctionItem => this.getFonctionIdentifier(fonctionItem)!);
      const fonctionsToAdd = fonctions.filter(fonctionItem => {
        const fonctionIdentifier = this.getFonctionIdentifier(fonctionItem);
        if (fonctionCollectionIdentifiers.includes(fonctionIdentifier)) {
          return false;
        }
        fonctionCollectionIdentifiers.push(fonctionIdentifier);
        return true;
      });
      return [...fonctionsToAdd, ...fonctionCollection];
    }
    return fonctionCollection;
  }
}
