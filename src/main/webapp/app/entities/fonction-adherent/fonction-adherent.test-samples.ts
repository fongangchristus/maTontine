import dayjs from 'dayjs/esm';

import { IFonctionAdherent, NewFonctionAdherent } from './fonction-adherent.model';

export const sampleWithRequiredData: IFonctionAdherent = {
  id: 42545,
  matriculeAdherent: 'help-desk',
  codeFonction: 'Pants Metal Assistant',
  datePriseFonction: dayjs('2023-06-08'),
  actif: false,
};

export const sampleWithPartialData: IFonctionAdherent = {
  id: 76975,
  matriculeAdherent: 'pink',
  codeFonction: 'Market optical',
  datePriseFonction: dayjs('2023-06-07'),
  dateFinFonction: dayjs('2023-06-08'),
  actif: true,
};

export const sampleWithFullData: IFonctionAdherent = {
  id: 75965,
  matriculeAdherent: 'blue Savings virtual',
  codeFonction: 'action-items invoice mobile',
  datePriseFonction: dayjs('2023-06-07'),
  dateFinFonction: dayjs('2023-06-08'),
  actif: false,
};

export const sampleWithNewData: NewFonctionAdherent = {
  matriculeAdherent: 'Pataca',
  codeFonction: 'integrate Boétie',
  datePriseFonction: dayjs('2023-06-08'),
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
