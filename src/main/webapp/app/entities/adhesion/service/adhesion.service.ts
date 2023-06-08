import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdhesion, NewAdhesion } from '../adhesion.model';

export type PartialUpdateAdhesion = Partial<IAdhesion> & Pick<IAdhesion, 'id'>;

type RestOf<T extends IAdhesion | NewAdhesion> = Omit<T, 'dateDebutAdhesion' | 'dateFinAdhesion'> & {
  dateDebutAdhesion?: string | null;
  dateFinAdhesion?: string | null;
};

export type RestAdhesion = RestOf<IAdhesion>;

export type NewRestAdhesion = RestOf<NewAdhesion>;

export type PartialUpdateRestAdhesion = RestOf<PartialUpdateAdhesion>;

export type EntityResponseType = HttpResponse<IAdhesion>;
export type EntityArrayResponseType = HttpResponse<IAdhesion[]>;

@Injectable({ providedIn: 'root' })
export class AdhesionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/adhesions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(adhesion: NewAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adhesion);
    return this.http
      .post<RestAdhesion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(adhesion: IAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adhesion);
    return this.http
      .put<RestAdhesion>(`${this.resourceUrl}/${this.getAdhesionIdentifier(adhesion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(adhesion: PartialUpdateAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adhesion);
    return this.http
      .patch<RestAdhesion>(`${this.resourceUrl}/${this.getAdhesionIdentifier(adhesion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAdhesion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAdhesion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdhesionIdentifier(adhesion: Pick<IAdhesion, 'id'>): number {
    return adhesion.id;
  }

  compareAdhesion(o1: Pick<IAdhesion, 'id'> | null, o2: Pick<IAdhesion, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdhesionIdentifier(o1) === this.getAdhesionIdentifier(o2) : o1 === o2;
  }

  addAdhesionToCollectionIfMissing<Type extends Pick<IAdhesion, 'id'>>(
    adhesionCollection: Type[],
    ...adhesionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const adhesions: Type[] = adhesionsToCheck.filter(isPresent);
    if (adhesions.length > 0) {
      const adhesionCollectionIdentifiers = adhesionCollection.map(adhesionItem => this.getAdhesionIdentifier(adhesionItem)!);
      const adhesionsToAdd = adhesions.filter(adhesionItem => {
        const adhesionIdentifier = this.getAdhesionIdentifier(adhesionItem);
        if (adhesionCollectionIdentifiers.includes(adhesionIdentifier)) {
          return false;
        }
        adhesionCollectionIdentifiers.push(adhesionIdentifier);
        return true;
      });
      return [...adhesionsToAdd, ...adhesionCollection];
    }
    return adhesionCollection;
  }

  protected convertDateFromClient<T extends IAdhesion | NewAdhesion | PartialUpdateAdhesion>(adhesion: T): RestOf<T> {
    return {
      ...adhesion,
      dateDebutAdhesion: adhesion.dateDebutAdhesion?.toJSON() ?? null,
      dateFinAdhesion: adhesion.dateFinAdhesion?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAdhesion: RestAdhesion): IAdhesion {
    return {
      ...restAdhesion,
      dateDebutAdhesion: restAdhesion.dateDebutAdhesion ? dayjs(restAdhesion.dateDebutAdhesion) : undefined,
      dateFinAdhesion: restAdhesion.dateFinAdhesion ? dayjs(restAdhesion.dateFinAdhesion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAdhesion>): HttpResponse<IAdhesion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAdhesion[]>): HttpResponse<IAdhesion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
