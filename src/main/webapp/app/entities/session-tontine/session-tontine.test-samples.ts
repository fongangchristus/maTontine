import dayjs from 'dayjs/esm';

import { ISessionTontine, NewSessionTontine } from './session-tontine.model';

export const sampleWithRequiredData: ISessionTontine = {
  id: 21901,
};

export const sampleWithPartialData: ISessionTontine = {
  id: 37297,
  dateFin: dayjs('2023-06-08'),
};

export const sampleWithFullData: ISessionTontine = {
  id: 98889,
  libelle: 'eyeballs next a',
  dateDebut: dayjs('2023-06-08'),
  dateFin: dayjs('2023-06-08'),
};

export const sampleWithNewData: NewSessionTontine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
