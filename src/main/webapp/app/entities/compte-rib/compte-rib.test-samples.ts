import { ICompteRIB, NewCompteRIB } from './compte-rib.model';

export const sampleWithRequiredData: ICompteRIB = {
  id: 76169,
};

export const sampleWithPartialData: ICompteRIB = {
  id: 28423,
  iban: 'HU24600604267785172610600857',
  titulaireCompte: 'user-centric',
};

export const sampleWithFullData: ICompteRIB = {
  id: 73736,
  iban: 'ES7600144620400080406648',
  titulaireCompte: 'Electronics deposit',
  verifier: false,
};

export const sampleWithNewData: NewCompteRIB = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
