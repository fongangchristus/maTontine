import { IMonnaie, NewMonnaie } from './monnaie.model';

export const sampleWithRequiredData: IMonnaie = {
  id: 14403,
};

export const sampleWithPartialData: IMonnaie = {
  id: 44487,
  libele: 'a Sausages',
};

export const sampleWithFullData: IMonnaie = {
  id: 83529,
  libele: 'maroon',
};

export const sampleWithNewData: NewMonnaie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
