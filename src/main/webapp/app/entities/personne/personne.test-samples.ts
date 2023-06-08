import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePersonne } from 'app/entities/enumerations/type-personne.model';

import { IPersonne, NewPersonne } from './personne.model';

export const sampleWithRequiredData: IPersonne = {
  id: 7936,
};

export const sampleWithPartialData: IPersonne = {
  id: 72015,
  idUser: 95506,
  matricule: 'Corse scale',
  nom: 'ivory',
  lieuNaissance: 48285,
  dateInscription: dayjs('2023-06-08T12:21'),
  profession: 'Panoramas',
  isAdmin: false,
  isDonateur: false,
  typePersonne: TypePersonne['ADHERENT'],
};

export const sampleWithFullData: IPersonne = {
  id: 49306,
  idUser: 94404,
  codeAssociation: 8955,
  matricule: 'redundant',
  nom: 'Account Handcrafted',
  prenom: 'Algérie web Fantastic',
  dateNaissance: dayjs('2023-06-07'),
  lieuNaissance: 91469,
  dateInscription: dayjs('2023-06-07T17:10'),
  profession: 'connecting Incredible clear-thinking',
  sexe: Sexe['FEMININ'],
  photoPath: 'Fresh Account',
  dateIntegration: dayjs('2023-06-08T06:41'),
  isAdmin: false,
  isDonateur: true,
  isBenevole: true,
  typePersonne: TypePersonne['ANCIEN_ADHERENT'],
};

export const sampleWithNewData: NewPersonne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
