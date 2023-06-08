import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssemble, NewAssemble } from '../assemble.model';

export type PartialUpdateAssemble = Partial<IAssemble> & Pick<IAssemble, 'id'>;

export type EntityResponseType = HttpResponse<IAssemble>;
export type EntityArrayResponseType = HttpResponse<IAssemble[]>;

@Injectable({ providedIn: 'root' })
export class AssembleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assembles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assemble: NewAssemble): Observable<EntityResponseType> {
    return this.http.post<IAssemble>(this.resourceUrl, assemble, { observe: 'response' });
  }

  update(assemble: IAssemble): Observable<EntityResponseType> {
    return this.http.put<IAssemble>(`${this.resourceUrl}/${this.getAssembleIdentifier(assemble)}`, assemble, { observe: 'response' });
  }

  partialUpdate(assemble: PartialUpdateAssemble): Observable<EntityResponseType> {
    return this.http.patch<IAssemble>(`${this.resourceUrl}/${this.getAssembleIdentifier(assemble)}`, assemble, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssemble>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssemble[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAssembleIdentifier(assemble: Pick<IAssemble, 'id'>): number {
    return assemble.id;
  }

  compareAssemble(o1: Pick<IAssemble, 'id'> | null, o2: Pick<IAssemble, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssembleIdentifier(o1) === this.getAssembleIdentifier(o2) : o1 === o2;
  }

  addAssembleToCollectionIfMissing<Type extends Pick<IAssemble, 'id'>>(
    assembleCollection: Type[],
    ...assemblesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assembles: Type[] = assemblesToCheck.filter(isPresent);
    if (assembles.length > 0) {
      const assembleCollectionIdentifiers = assembleCollection.map(assembleItem => this.getAssembleIdentifier(assembleItem)!);
      const assemblesToAdd = assembles.filter(assembleItem => {
        const assembleIdentifier = this.getAssembleIdentifier(assembleItem);
        if (assembleCollectionIdentifiers.includes(assembleIdentifier)) {
          return false;
        }
        assembleCollectionIdentifiers.push(assembleIdentifier);
        return true;
      });
      return [...assemblesToAdd, ...assembleCollection];
    }
    return assembleCollection;
  }
}
