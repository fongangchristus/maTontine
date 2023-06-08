import dayjs from 'dayjs/esm';

import { IGestionnaireTontine, NewGestionnaireTontine } from './gestionnaire-tontine.model';

export const sampleWithRequiredData: IGestionnaireTontine = {
  id: 8929,
};

export const sampleWithPartialData: IGestionnaireTontine = {
  id: 491,
  matriculeAdherent: 'driver HTTP',
  codeTontine: 'dynamic Unbranded',
  datePriseFonction: dayjs('2023-06-07'),
};

export const sampleWithFullData: IGestionnaireTontine = {
  id: 73802,
  matriculeAdherent: 'redefine',
  codeTontine: 'focus',
  datePriseFonction: dayjs('2023-06-07'),
  dateFinFonction: dayjs('2023-06-08'),
};

export const sampleWithNewData: NewGestionnaireTontine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
