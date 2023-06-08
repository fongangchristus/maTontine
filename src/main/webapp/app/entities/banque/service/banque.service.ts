import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBanque, NewBanque } from '../banque.model';

export type PartialUpdateBanque = Partial<IBanque> & Pick<IBanque, 'id'>;

type RestOf<T extends IBanque | NewBanque> = Omit<T, 'dateOuverture' | 'dateCloture'> & {
  dateOuverture?: string | null;
  dateCloture?: string | null;
};

export type RestBanque = RestOf<IBanque>;

export type NewRestBanque = RestOf<NewBanque>;

export type PartialUpdateRestBanque = RestOf<PartialUpdateBanque>;

export type EntityResponseType = HttpResponse<IBanque>;
export type EntityArrayResponseType = HttpResponse<IBanque[]>;

@Injectable({ providedIn: 'root' })
export class BanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(banque: NewBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banque);
    return this.http
      .post<RestBanque>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(banque: IBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banque);
    return this.http
      .put<RestBanque>(`${this.resourceUrl}/${this.getBanqueIdentifier(banque)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(banque: PartialUpdateBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banque);
    return this.http
      .patch<RestBanque>(`${this.resourceUrl}/${this.getBanqueIdentifier(banque)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBanque[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBanqueIdentifier(banque: Pick<IBanque, 'id'>): number {
    return banque.id;
  }

  compareBanque(o1: Pick<IBanque, 'id'> | null, o2: Pick<IBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getBanqueIdentifier(o1) === this.getBanqueIdentifier(o2) : o1 === o2;
  }

  addBanqueToCollectionIfMissing<Type extends Pick<IBanque, 'id'>>(
    banqueCollection: Type[],
    ...banquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const banques: Type[] = banquesToCheck.filter(isPresent);
    if (banques.length > 0) {
      const banqueCollectionIdentifiers = banqueCollection.map(banqueItem => this.getBanqueIdentifier(banqueItem)!);
      const banquesToAdd = banques.filter(banqueItem => {
        const banqueIdentifier = this.getBanqueIdentifier(banqueItem);
        if (banqueCollectionIdentifiers.includes(banqueIdentifier)) {
          return false;
        }
        banqueCollectionIdentifiers.push(banqueIdentifier);
        return true;
      });
      return [...banquesToAdd, ...banqueCollection];
    }
    return banqueCollection;
  }

  protected convertDateFromClient<T extends IBanque | NewBanque | PartialUpdateBanque>(banque: T): RestOf<T> {
    return {
      ...banque,
      dateOuverture: banque.dateOuverture?.toJSON() ?? null,
      dateCloture: banque.dateCloture?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBanque: RestBanque): IBanque {
    return {
      ...restBanque,
      dateOuverture: restBanque.dateOuverture ? dayjs(restBanque.dateOuverture) : undefined,
      dateCloture: restBanque.dateCloture ? dayjs(restBanque.dateCloture) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBanque>): HttpResponse<IBanque> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBanque[]>): HttpResponse<IBanque[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
