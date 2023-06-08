import dayjs from 'dayjs/esm';

import { IDecaisementBanque, NewDecaisementBanque } from './decaisement-banque.model';

export const sampleWithRequiredData: IDecaisementBanque = {
  id: 77244,
};

export const sampleWithPartialData: IDecaisementBanque = {
  id: 23152,
  dateDecaissement: dayjs('2023-06-07T18:32'),
  commentaire: 'cross-platform Berkshire deposit',
};

export const sampleWithFullData: IDecaisementBanque = {
  id: 94375,
  libelle: 'b Computer Fully-configurable',
  montant: 47490,
  dateDecaissement: dayjs('2023-06-07T19:03'),
  montantDecaisse: 37774,
  commentaire: 'base',
};

export const sampleWithNewData: NewDecaisementBanque = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
