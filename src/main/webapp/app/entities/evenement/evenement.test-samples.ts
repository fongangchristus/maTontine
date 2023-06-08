import dayjs from 'dayjs/esm';

import { IEvenement, NewEvenement } from './evenement.model';

export const sampleWithRequiredData: IEvenement = {
  id: 59909,
};

export const sampleWithPartialData: IEvenement = {
  id: 79527,
  libele: 'a',
  budget: 46104,
  dateEvenement: dayjs('2023-06-08T03:20'),
};

export const sampleWithFullData: IEvenement = {
  id: 87795,
  libele: 'Rhône-Alpes Agent tertiary',
  codepot: 'invoice Zadkine',
  montantPayer: 'optimize PCI Avon',
  description: 'Account',
  budget: 28456,
  dateEvenement: dayjs('2023-06-07T22:25'),
};

export const sampleWithNewData: NewEvenement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
