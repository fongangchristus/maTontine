import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISanction, NewSanction } from '../sanction.model';

export type PartialUpdateSanction = Partial<ISanction> & Pick<ISanction, 'id'>;

type RestOf<T extends ISanction | NewSanction> = Omit<T, 'dateSanction'> & {
  dateSanction?: string | null;
};

export type RestSanction = RestOf<ISanction>;

export type NewRestSanction = RestOf<NewSanction>;

export type PartialUpdateRestSanction = RestOf<PartialUpdateSanction>;

export type EntityResponseType = HttpResponse<ISanction>;
export type EntityArrayResponseType = HttpResponse<ISanction[]>;

@Injectable({ providedIn: 'root' })
export class SanctionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sanctions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sanction: NewSanction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanction);
    return this.http
      .post<RestSanction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sanction: ISanction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanction);
    return this.http
      .put<RestSanction>(`${this.resourceUrl}/${this.getSanctionIdentifier(sanction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sanction: PartialUpdateSanction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanction);
    return this.http
      .patch<RestSanction>(`${this.resourceUrl}/${this.getSanctionIdentifier(sanction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSanction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSanction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSanctionIdentifier(sanction: Pick<ISanction, 'id'>): number {
    return sanction.id;
  }

  compareSanction(o1: Pick<ISanction, 'id'> | null, o2: Pick<ISanction, 'id'> | null): boolean {
    return o1 && o2 ? this.getSanctionIdentifier(o1) === this.getSanctionIdentifier(o2) : o1 === o2;
  }

  addSanctionToCollectionIfMissing<Type extends Pick<ISanction, 'id'>>(
    sanctionCollection: Type[],
    ...sanctionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sanctions: Type[] = sanctionsToCheck.filter(isPresent);
    if (sanctions.length > 0) {
      const sanctionCollectionIdentifiers = sanctionCollection.map(sanctionItem => this.getSanctionIdentifier(sanctionItem)!);
      const sanctionsToAdd = sanctions.filter(sanctionItem => {
        const sanctionIdentifier = this.getSanctionIdentifier(sanctionItem);
        if (sanctionCollectionIdentifiers.includes(sanctionIdentifier)) {
          return false;
        }
        sanctionCollectionIdentifiers.push(sanctionIdentifier);
        return true;
      });
      return [...sanctionsToAdd, ...sanctionCollection];
    }
    return sanctionCollection;
  }

  protected convertDateFromClient<T extends ISanction | NewSanction | PartialUpdateSanction>(sanction: T): RestOf<T> {
    return {
      ...sanction,
      dateSanction: sanction.dateSanction?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSanction: RestSanction): ISanction {
    return {
      ...restSanction,
      dateSanction: restSanction.dateSanction ? dayjs(restSanction.dateSanction) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSanction>): HttpResponse<ISanction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSanction[]>): HttpResponse<ISanction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
