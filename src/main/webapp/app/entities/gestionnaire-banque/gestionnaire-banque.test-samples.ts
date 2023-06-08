import { IGestionnaireBanque, NewGestionnaireBanque } from './gestionnaire-banque.model';

export const sampleWithRequiredData: IGestionnaireBanque = {
  id: 81628,
};

export const sampleWithPartialData: IGestionnaireBanque = {
  id: 54081,
  matriculeMembre: 'Émirats architectures',
};

export const sampleWithFullData: IGestionnaireBanque = {
  id: 52861,
  matriculeMembre: 'Viêt generate',
};

export const sampleWithNewData: NewGestionnaireBanque = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
