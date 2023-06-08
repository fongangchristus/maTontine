import { ICompteBanque, NewCompteBanque } from './compte-banque.model';

export const sampleWithRequiredData: ICompteBanque = {
  id: 22828,
};

export const sampleWithPartialData: ICompteBanque = {
  id: 36188,
  libelle: 'program',
  matriculeAdherant: 'solution withdrawal Devolved',
};

export const sampleWithFullData: ICompteBanque = {
  id: 81962,
  libelle: 'e-commerce',
  description: 'e-markets Executif',
  matriculeAdherant: 'Swaziland Berkshire',
  montantDisponnible: 2239,
};

export const sampleWithNewData: NewCompteBanque = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
