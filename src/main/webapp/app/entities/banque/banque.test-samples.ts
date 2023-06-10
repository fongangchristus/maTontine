import dayjs from 'dayjs/esm';

import { IBanque, NewBanque } from './banque.model';

export const sampleWithRequiredData: IBanque = {
  id: 83469,
  codeAssociation: 'Laos',
};

export const sampleWithPartialData: IBanque = {
  id: 56991,
  codeAssociation: 'Nam azure',
  dateOuverture: dayjs('2023-06-07T17:37'),
  dateCloture: dayjs('2023-06-08T00:25'),
  penaliteRetardRnbrsmnt: 6055,
};

export const sampleWithFullData: IBanque = {
  id: 22726,
  codeAssociation: 'Automotive monetize capacitor',
  libelle: 'Technicien Afghani des',
  description: 'payment',
  dateOuverture: dayjs('2023-06-08T10:20'),
  dateCloture: dayjs('2023-06-07T23:35'),
  penaliteRetardRnbrsmnt: 36915,
  tauxInteretPret: 68492,
};

export const sampleWithNewData: NewBanque = {
  codeAssociation: 'intuitive Intelligent Buckinghamshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
