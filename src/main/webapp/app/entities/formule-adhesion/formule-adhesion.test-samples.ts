import dayjs from 'dayjs/esm';

import { IFormuleAdhesion, NewFormuleAdhesion } from './formule-adhesion.model';

export const sampleWithRequiredData: IFormuleAdhesion = {
  id: 92765,
};

export const sampleWithPartialData: IFormuleAdhesion = {
  id: 35571,
  dateDebut: dayjs('2023-06-07T23:45'),
  montantLibre: false,
  tarif: 68158,
};

export const sampleWithFullData: IFormuleAdhesion = {
  id: 75086,
  adhesionPeriodique: false,
  dateDebut: dayjs('2023-06-07T19:26'),
  dureeAdhesionMois: 58175,
  montantLibre: false,
  description: 'Zadkine',
  tarif: 34937,
};

export const sampleWithNewData: NewFormuleAdhesion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
