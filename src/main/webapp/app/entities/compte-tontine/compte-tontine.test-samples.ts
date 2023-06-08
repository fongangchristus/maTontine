import { ICompteTontine, NewCompteTontine } from './compte-tontine.model';

export const sampleWithRequiredData: ICompteTontine = {
  id: 79162,
  matriculeCompte: 'infomediaries action-items teal',
  matriculeMenbre: 'Unbranded Checking',
};

export const sampleWithPartialData: ICompteTontine = {
  id: 89910,
  etatDeCompte: true,
  libele: 'morph Basse-Normandie',
  matriculeCompte: 'Small magenta',
  matriculeMenbre: 'primary',
};

export const sampleWithFullData: ICompteTontine = {
  id: 37893,
  etatDeCompte: false,
  libele: 'Wooden Mauritius',
  odreBeneficiere: 67502,
  matriculeCompte: 'yellow',
  matriculeMenbre: 'Investment client-server',
};

export const sampleWithNewData: NewCompteTontine = {
  matriculeCompte: 'Stagiaire Account Cambridgeshire',
  matriculeMenbre: 'invoice Buckinghamshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
