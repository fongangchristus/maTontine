import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFonctionAdherent, NewFonctionAdherent } from '../fonction-adherent.model';

export type PartialUpdateFonctionAdherent = Partial<IFonctionAdherent> & Pick<IFonctionAdherent, 'id'>;

type RestOf<T extends IFonctionAdherent | NewFonctionAdherent> = Omit<T, 'datePriseFonction' | 'dateFinFonction'> & {
  datePriseFonction?: string | null;
  dateFinFonction?: string | null;
};

export type RestFonctionAdherent = RestOf<IFonctionAdherent>;

export type NewRestFonctionAdherent = RestOf<NewFonctionAdherent>;

export type PartialUpdateRestFonctionAdherent = RestOf<PartialUpdateFonctionAdherent>;

export type EntityResponseType = HttpResponse<IFonctionAdherent>;
export type EntityArrayResponseType = HttpResponse<IFonctionAdherent[]>;

@Injectable({ providedIn: 'root' })
export class FonctionAdherentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fonction-adherents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fonctionAdherent: NewFonctionAdherent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fonctionAdherent);
    return this.http
      .post<RestFonctionAdherent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fonctionAdherent: IFonctionAdherent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fonctionAdherent);
    return this.http
      .put<RestFonctionAdherent>(`${this.resourceUrl}/${this.getFonctionAdherentIdentifier(fonctionAdherent)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fonctionAdherent: PartialUpdateFonctionAdherent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fonctionAdherent);
    return this.http
      .patch<RestFonctionAdherent>(`${this.resourceUrl}/${this.getFonctionAdherentIdentifier(fonctionAdherent)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFonctionAdherent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFonctionAdherent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFonctionAdherentIdentifier(fonctionAdherent: Pick<IFonctionAdherent, 'id'>): number {
    return fonctionAdherent.id;
  }

  compareFonctionAdherent(o1: Pick<IFonctionAdherent, 'id'> | null, o2: Pick<IFonctionAdherent, 'id'> | null): boolean {
    return o1 && o2 ? this.getFonctionAdherentIdentifier(o1) === this.getFonctionAdherentIdentifier(o2) : o1 === o2;
  }

  addFonctionAdherentToCollectionIfMissing<Type extends Pick<IFonctionAdherent, 'id'>>(
    fonctionAdherentCollection: Type[],
    ...fonctionAdherentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fonctionAdherents: Type[] = fonctionAdherentsToCheck.filter(isPresent);
    if (fonctionAdherents.length > 0) {
      const fonctionAdherentCollectionIdentifiers = fonctionAdherentCollection.map(
        fonctionAdherentItem => this.getFonctionAdherentIdentifier(fonctionAdherentItem)!
      );
      const fonctionAdherentsToAdd = fonctionAdherents.filter(fonctionAdherentItem => {
        const fonctionAdherentIdentifier = this.getFonctionAdherentIdentifier(fonctionAdherentItem);
        if (fonctionAdherentCollectionIdentifiers.includes(fonctionAdherentIdentifier)) {
          return false;
        }
        fonctionAdherentCollectionIdentifiers.push(fonctionAdherentIdentifier);
        return true;
      });
      return [...fonctionAdherentsToAdd, ...fonctionAdherentCollection];
    }
    return fonctionAdherentCollection;
  }

  protected convertDateFromClient<T extends IFonctionAdherent | NewFonctionAdherent | PartialUpdateFonctionAdherent>(
    fonctionAdherent: T
  ): RestOf<T> {
    return {
      ...fonctionAdherent,
      datePriseFonction: fonctionAdherent.datePriseFonction?.format(DATE_FORMAT) ?? null,
      dateFinFonction: fonctionAdherent.dateFinFonction?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFonctionAdherent: RestFonctionAdherent): IFonctionAdherent {
    return {
      ...restFonctionAdherent,
      datePriseFonction: restFonctionAdherent.datePriseFonction ? dayjs(restFonctionAdherent.datePriseFonction) : undefined,
      dateFinFonction: restFonctionAdherent.dateFinFonction ? dayjs(restFonctionAdherent.dateFinFonction) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFonctionAdherent>): HttpResponse<IFonctionAdherent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFonctionAdherent[]>): HttpResponse<IFonctionAdherent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
