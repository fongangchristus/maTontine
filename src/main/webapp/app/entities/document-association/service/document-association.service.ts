import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentAssociation, NewDocumentAssociation } from '../document-association.model';

export type PartialUpdateDocumentAssociation = Partial<IDocumentAssociation> & Pick<IDocumentAssociation, 'id'>;

type RestOf<T extends IDocumentAssociation | NewDocumentAssociation> = Omit<T, 'dateEnregistrement' | 'dateArchivage'> & {
  dateEnregistrement?: string | null;
  dateArchivage?: string | null;
};

export type RestDocumentAssociation = RestOf<IDocumentAssociation>;

export type NewRestDocumentAssociation = RestOf<NewDocumentAssociation>;

export type PartialUpdateRestDocumentAssociation = RestOf<PartialUpdateDocumentAssociation>;

export type EntityResponseType = HttpResponse<IDocumentAssociation>;
export type EntityArrayResponseType = HttpResponse<IDocumentAssociation[]>;

@Injectable({ providedIn: 'root' })
export class DocumentAssociationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-associations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentAssociation: NewDocumentAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAssociation);
    return this.http
      .post<RestDocumentAssociation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentAssociation: IDocumentAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAssociation);
    return this.http
      .put<RestDocumentAssociation>(`${this.resourceUrl}/${this.getDocumentAssociationIdentifier(documentAssociation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentAssociation: PartialUpdateDocumentAssociation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAssociation);
    return this.http
      .patch<RestDocumentAssociation>(`${this.resourceUrl}/${this.getDocumentAssociationIdentifier(documentAssociation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentAssociation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentAssociation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDocumentAssociationIdentifier(documentAssociation: Pick<IDocumentAssociation, 'id'>): number {
    return documentAssociation.id;
  }

  compareDocumentAssociation(o1: Pick<IDocumentAssociation, 'id'> | null, o2: Pick<IDocumentAssociation, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentAssociationIdentifier(o1) === this.getDocumentAssociationIdentifier(o2) : o1 === o2;
  }

  addDocumentAssociationToCollectionIfMissing<Type extends Pick<IDocumentAssociation, 'id'>>(
    documentAssociationCollection: Type[],
    ...documentAssociationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentAssociations: Type[] = documentAssociationsToCheck.filter(isPresent);
    if (documentAssociations.length > 0) {
      const documentAssociationCollectionIdentifiers = documentAssociationCollection.map(
        documentAssociationItem => this.getDocumentAssociationIdentifier(documentAssociationItem)!
      );
      const documentAssociationsToAdd = documentAssociations.filter(documentAssociationItem => {
        const documentAssociationIdentifier = this.getDocumentAssociationIdentifier(documentAssociationItem);
        if (documentAssociationCollectionIdentifiers.includes(documentAssociationIdentifier)) {
          return false;
        }
        documentAssociationCollectionIdentifiers.push(documentAssociationIdentifier);
        return true;
      });
      return [...documentAssociationsToAdd, ...documentAssociationCollection];
    }
    return documentAssociationCollection;
  }

  protected convertDateFromClient<T extends IDocumentAssociation | NewDocumentAssociation | PartialUpdateDocumentAssociation>(
    documentAssociation: T
  ): RestOf<T> {
    return {
      ...documentAssociation,
      dateEnregistrement: documentAssociation.dateEnregistrement?.format(DATE_FORMAT) ?? null,
      dateArchivage: documentAssociation.dateArchivage?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDocumentAssociation: RestDocumentAssociation): IDocumentAssociation {
    return {
      ...restDocumentAssociation,
      dateEnregistrement: restDocumentAssociation.dateEnregistrement ? dayjs(restDocumentAssociation.dateEnregistrement) : undefined,
      dateArchivage: restDocumentAssociation.dateArchivage ? dayjs(restDocumentAssociation.dateArchivage) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentAssociation>): HttpResponse<IDocumentAssociation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentAssociation[]>): HttpResponse<IDocumentAssociation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
