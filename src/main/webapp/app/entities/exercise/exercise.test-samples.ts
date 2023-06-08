import dayjs from 'dayjs/esm';

import { StatutExercice } from 'app/entities/enumerations/statut-exercice.model';

import { IExercise, NewExercise } from './exercise.model';

export const sampleWithRequiredData: IExercise = {
  id: 13640,
};

export const sampleWithPartialData: IExercise = {
  id: 6295,
  libele: 'open-source envisioneer',
  observation: 'Compatible encoding hacking',
  dateDebut: dayjs('2023-06-07'),
  statut: StatutExercice['FERME'],
};

export const sampleWithFullData: IExercise = {
  id: 50558,
  libele: 'bluetooth transmitting Molière',
  observation: 'Viêt b heuristic',
  dateDebut: dayjs('2023-06-08'),
  dateFin: dayjs('2023-06-08'),
  statut: StatutExercice['OUVERT'],
};

export const sampleWithNewData: NewExercise = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
