import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePersonne } from 'app/entities/enumerations/type-personne.model';

import { IPersonne, NewPersonne } from './personne.model';

export const sampleWithRequiredData: IPersonne = {
  id: 7936,
  telephone: '+33 208824978',
};

export const sampleWithPartialData: IPersonne = {
  id: 18696,
  nom: 'infrastructure turquoise Belize',
  prenom: 'Cambridgeshire',
  telephone: '0374146271',
  dateNaissance: dayjs('2023-06-07'),
  lieuNaissance: 13472,
  dateInscription: dayjs('2023-06-07T16:43'),
  profession: 'Clothing a',
  sexe: Sexe['MASCULIN'],
  dateIntegration: dayjs('2023-06-08T00:02'),
  isDonateur: true,
};

export const sampleWithFullData: IPersonne = {
  id: 91469,
  idUser: 89325,
  codeAssociation: 78216,
  matricule: 'Berkshire parallelism Home',
  nom: 'Languedoc-Roussillon',
  prenom: 'Loan a Account',
  telephone: '+33 384500394',
  email: 'Astrie46@hotmail.fr',
  dateNaissance: dayjs('2023-06-07'),
  lieuNaissance: 21045,
  dateInscription: dayjs('2023-06-07T16:41'),
  profession: 'Generic',
  sexe: Sexe['FEMININ'],
  photoPath: 'drive bandwidth',
  dateIntegration: dayjs('2023-06-07T19:05'),
  isAdmin: true,
  isDonateur: false,
  isBenevole: true,
  typePersonne: TypePersonne['ADHERENT'],
};

export const sampleWithNewData: NewPersonne = {
  telephone: '+33 676417585',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
