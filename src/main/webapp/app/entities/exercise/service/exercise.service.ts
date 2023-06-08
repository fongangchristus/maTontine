import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExercise, NewExercise } from '../exercise.model';

export type PartialUpdateExercise = Partial<IExercise> & Pick<IExercise, 'id'>;

type RestOf<T extends IExercise | NewExercise> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestExercise = RestOf<IExercise>;

export type NewRestExercise = RestOf<NewExercise>;

export type PartialUpdateRestExercise = RestOf<PartialUpdateExercise>;

export type EntityResponseType = HttpResponse<IExercise>;
export type EntityArrayResponseType = HttpResponse<IExercise[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exercises');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exercise: NewExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exercise);
    return this.http
      .post<RestExercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exercise: IExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exercise);
    return this.http
      .put<RestExercise>(`${this.resourceUrl}/${this.getExerciseIdentifier(exercise)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exercise: PartialUpdateExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exercise);
    return this.http
      .patch<RestExercise>(`${this.resourceUrl}/${this.getExerciseIdentifier(exercise)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExercise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExercise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExerciseIdentifier(exercise: Pick<IExercise, 'id'>): number {
    return exercise.id;
  }

  compareExercise(o1: Pick<IExercise, 'id'> | null, o2: Pick<IExercise, 'id'> | null): boolean {
    return o1 && o2 ? this.getExerciseIdentifier(o1) === this.getExerciseIdentifier(o2) : o1 === o2;
  }

  addExerciseToCollectionIfMissing<Type extends Pick<IExercise, 'id'>>(
    exerciseCollection: Type[],
    ...exercisesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exercises: Type[] = exercisesToCheck.filter(isPresent);
    if (exercises.length > 0) {
      const exerciseCollectionIdentifiers = exerciseCollection.map(exerciseItem => this.getExerciseIdentifier(exerciseItem)!);
      const exercisesToAdd = exercises.filter(exerciseItem => {
        const exerciseIdentifier = this.getExerciseIdentifier(exerciseItem);
        if (exerciseCollectionIdentifiers.includes(exerciseIdentifier)) {
          return false;
        }
        exerciseCollectionIdentifiers.push(exerciseIdentifier);
        return true;
      });
      return [...exercisesToAdd, ...exerciseCollection];
    }
    return exerciseCollection;
  }

  protected convertDateFromClient<T extends IExercise | NewExercise | PartialUpdateExercise>(exercise: T): RestOf<T> {
    return {
      ...exercise,
      dateDebut: exercise.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: exercise.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restExercise: RestExercise): IExercise {
    return {
      ...restExercise,
      dateDebut: restExercise.dateDebut ? dayjs(restExercise.dateDebut) : undefined,
      dateFin: restExercise.dateFin ? dayjs(restExercise.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExercise>): HttpResponse<IExercise> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExercise[]>): HttpResponse<IExercise[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
