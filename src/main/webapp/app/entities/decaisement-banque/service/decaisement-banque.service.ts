import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecaisementBanque, NewDecaisementBanque } from '../decaisement-banque.model';

export type PartialUpdateDecaisementBanque = Partial<IDecaisementBanque> & Pick<IDecaisementBanque, 'id'>;

type RestOf<T extends IDecaisementBanque | NewDecaisementBanque> = Omit<T, 'dateDecaissement'> & {
  dateDecaissement?: string | null;
};

export type RestDecaisementBanque = RestOf<IDecaisementBanque>;

export type NewRestDecaisementBanque = RestOf<NewDecaisementBanque>;

export type PartialUpdateRestDecaisementBanque = RestOf<PartialUpdateDecaisementBanque>;

export type EntityResponseType = HttpResponse<IDecaisementBanque>;
export type EntityArrayResponseType = HttpResponse<IDecaisementBanque[]>;

@Injectable({ providedIn: 'root' })
export class DecaisementBanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decaisement-banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(decaisementBanque: NewDecaisementBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaisementBanque);
    return this.http
      .post<RestDecaisementBanque>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(decaisementBanque: IDecaisementBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaisementBanque);
    return this.http
      .put<RestDecaisementBanque>(`${this.resourceUrl}/${this.getDecaisementBanqueIdentifier(decaisementBanque)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(decaisementBanque: PartialUpdateDecaisementBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaisementBanque);
    return this.http
      .patch<RestDecaisementBanque>(`${this.resourceUrl}/${this.getDecaisementBanqueIdentifier(decaisementBanque)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDecaisementBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDecaisementBanque[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDecaisementBanqueIdentifier(decaisementBanque: Pick<IDecaisementBanque, 'id'>): number {
    return decaisementBanque.id;
  }

  compareDecaisementBanque(o1: Pick<IDecaisementBanque, 'id'> | null, o2: Pick<IDecaisementBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getDecaisementBanqueIdentifier(o1) === this.getDecaisementBanqueIdentifier(o2) : o1 === o2;
  }

  addDecaisementBanqueToCollectionIfMissing<Type extends Pick<IDecaisementBanque, 'id'>>(
    decaisementBanqueCollection: Type[],
    ...decaisementBanquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const decaisementBanques: Type[] = decaisementBanquesToCheck.filter(isPresent);
    if (decaisementBanques.length > 0) {
      const decaisementBanqueCollectionIdentifiers = decaisementBanqueCollection.map(
        decaisementBanqueItem => this.getDecaisementBanqueIdentifier(decaisementBanqueItem)!
      );
      const decaisementBanquesToAdd = decaisementBanques.filter(decaisementBanqueItem => {
        const decaisementBanqueIdentifier = this.getDecaisementBanqueIdentifier(decaisementBanqueItem);
        if (decaisementBanqueCollectionIdentifiers.includes(decaisementBanqueIdentifier)) {
          return false;
        }
        decaisementBanqueCollectionIdentifiers.push(decaisementBanqueIdentifier);
        return true;
      });
      return [...decaisementBanquesToAdd, ...decaisementBanqueCollection];
    }
    return decaisementBanqueCollection;
  }

  protected convertDateFromClient<T extends IDecaisementBanque | NewDecaisementBanque | PartialUpdateDecaisementBanque>(
    decaisementBanque: T
  ): RestOf<T> {
    return {
      ...decaisementBanque,
      dateDecaissement: decaisementBanque.dateDecaissement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDecaisementBanque: RestDecaisementBanque): IDecaisementBanque {
    return {
      ...restDecaisementBanque,
      dateDecaissement: restDecaisementBanque.dateDecaissement ? dayjs(restDecaisementBanque.dateDecaissement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDecaisementBanque>): HttpResponse<IDecaisementBanque> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDecaisementBanque[]>): HttpResponse<IDecaisementBanque[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
