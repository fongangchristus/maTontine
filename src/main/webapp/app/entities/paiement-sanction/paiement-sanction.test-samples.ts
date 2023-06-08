import { IPaiementSanction, NewPaiementSanction } from './paiement-sanction.model';

export const sampleWithRequiredData: IPaiementSanction = {
  id: 94211,
  referencePaiement: 'cross-platform Manager',
};

export const sampleWithPartialData: IPaiementSanction = {
  id: 49195,
  referencePaiement: 'Barbade Rustic systematic',
};

export const sampleWithFullData: IPaiementSanction = {
  id: 68651,
  referencePaiement: 'leading-edge cross-media incubate',
};

export const sampleWithNewData: NewPaiementSanction = {
  referencePaiement: 'Granite Awesome haptic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
