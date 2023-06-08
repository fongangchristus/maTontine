import dayjs from 'dayjs/esm';

import { IDecaissementTontine, NewDecaissementTontine } from './decaissement-tontine.model';

export const sampleWithRequiredData: IDecaissementTontine = {
  id: 95540,
};

export const sampleWithPartialData: IDecaissementTontine = {
  id: 76650,
  libele: 'input Soft',
  dateDecaissement: dayjs('2023-06-07'),
  commentaire: 'du intuitive microchip',
};

export const sampleWithFullData: IDecaissementTontine = {
  id: 21757,
  libele: 'generating Tasty',
  dateDecaissement: dayjs('2023-06-07'),
  montantDecaisse: 55061,
  commentaire: 'Manat Checking',
};

export const sampleWithNewData: NewDecaissementTontine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
