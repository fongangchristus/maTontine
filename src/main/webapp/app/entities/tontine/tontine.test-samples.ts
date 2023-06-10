import dayjs from 'dayjs/esm';

import { TypePenalite } from 'app/entities/enumerations/type-penalite.model';
import { StatutTontine } from 'app/entities/enumerations/statut-tontine.model';

import { ITontine, NewTontine } from './tontine.model';

export const sampleWithRequiredData: ITontine = {
  id: 76527,
  codeAssociation: 'EXE Outdoors',
};

export const sampleWithPartialData: ITontine = {
  id: 37694,
  codeAssociation: 'middleware blue',
  nombreTour: 89697,
  montantCagnote: 78917,
  penaliteRetardCotisation: 89421,
  dateCreation: dayjs('2023-06-08'),
  datePremierTour: dayjs('2023-06-07'),
};

export const sampleWithFullData: ITontine = {
  id: 12740,
  codeAssociation: 'c',
  libele: 'Wooden',
  nombreTour: 15043,
  nombrePersonne: 75075,
  margeBeneficiaire: 80437,
  montantPart: 71807,
  montantCagnote: 56945,
  penaliteRetardCotisation: 76412,
  typePenalite: TypePenalite['POURCENTAGE'],
  dateCreation: dayjs('2023-06-07'),
  datePremierTour: dayjs('2023-06-08'),
  dateDernierTour: dayjs('2023-06-08'),
  statutTontine: StatutTontine['OUVERTE'],
  description: 'generating Lari',
};

export const sampleWithNewData: NewTontine = {
  codeAssociation: "THX Pa'anga Baby",
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
