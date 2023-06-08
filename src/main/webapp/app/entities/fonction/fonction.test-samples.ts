import { IFonction, NewFonction } from './fonction.model';

export const sampleWithRequiredData: IFonction = {
  id: 57026,
};

export const sampleWithPartialData: IFonction = {
  id: 11235,
  description: 'Profit-focused Wooden',
};

export const sampleWithFullData: IFonction = {
  id: 7635,
  title: 'cyan port',
  description: 'Beauty',
};

export const sampleWithNewData: NewFonction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
