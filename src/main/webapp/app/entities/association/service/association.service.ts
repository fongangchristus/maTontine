import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssociation, NewAssociation } from '../association.model';

export type PartialUpdateAssociation = Partial<IAssociation> & Pick<IAssociation, 'id'>;

type RestOf<T extends IAssociation | NewAssociation> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestAssociation = RestOf<IAssociation>;

export type NewRestAssociation = RestOf<NewAssociation>;

export type PartialUpdateRestAssociation = RestOf<PartialUpdateAssociation>;

export type EntityResponseType = HttpResponse<IAssociation>;
export type EntityArrayResponseType = HttpResponse<IAssociation[]>;

@Injectable({ providedIn: 'root' })
export class AssociationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/associations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(association: NewAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(association);
    return this.http
      .post<RestAssociation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(association: IAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(association);
    return this.http
      .put<RestAssociation>(`${this.resourceUrl}/${this.getAssociationIdentifier(association)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(association: PartialUpdateAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(association);
    return this.http
      .patch<RestAssociation>(`${this.resourceUrl}/${this.getAssociationIdentifier(association)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAssociation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAssociation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAssociationIdentifier(association: Pick<IAssociation, 'id'>): number {
    return association.id;
  }

  compareAssociation(o1: Pick<IAssociation, 'id'> | null, o2: Pick<IAssociation, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssociationIdentifier(o1) === this.getAssociationIdentifier(o2) : o1 === o2;
  }

  addAssociationToCollectionIfMissing<Type extends Pick<IAssociation, 'id'>>(
    associationCollection: Type[],
    ...associationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const associations: Type[] = associationsToCheck.filter(isPresent);
    if (associations.length > 0) {
      const associationCollectionIdentifiers = associationCollection.map(
        associationItem => this.getAssociationIdentifier(associationItem)!
      );
      const associationsToAdd = associations.filter(associationItem => {
        const associationIdentifier = this.getAssociationIdentifier(associationItem);
        if (associationCollectionIdentifiers.includes(associationIdentifier)) {
          return false;
        }
        associationCollectionIdentifiers.push(associationIdentifier);
        return true;
      });
      return [...associationsToAdd, ...associationCollection];
    }
    return associationCollection;
  }

  protected convertDateFromClient<T extends IAssociation | NewAssociation | PartialUpdateAssociation>(association: T): RestOf<T> {
    return {
      ...association,
      dateCreation: association.dateCreation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAssociation: RestAssociation): IAssociation {
    return {
      ...restAssociation,
      dateCreation: restAssociation.dateCreation ? dayjs(restAssociation.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAssociation>): HttpResponse<IAssociation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAssociation[]>): HttpResponse<IAssociation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
