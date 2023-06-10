import dayjs from 'dayjs/esm';

import { StatutAdhesion } from 'app/entities/enumerations/statut-adhesion.model';

import { IAdhesion, NewAdhesion } from './adhesion.model';

export const sampleWithRequiredData: IAdhesion = {
  id: 80252,
};

export const sampleWithPartialData: IAdhesion = {
  id: 98596,
  matriculePersonne: 'purple',
};

export const sampleWithFullData: IAdhesion = {
  id: 7444,
  statutAdhesion: StatutAdhesion['ACTIVE'],
  matriculePersonne: 'deposit teal',
  dateDebutAdhesion: dayjs('2023-06-08T06:50'),
  dateFinAdhesion: dayjs('2023-06-08T14:19'),
};

export const sampleWithNewData: NewAdhesion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
