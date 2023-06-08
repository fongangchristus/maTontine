import { IAdresse, NewAdresse } from './adresse.model';

export const sampleWithRequiredData: IAdresse = {
  id: 36217,
  departmentName: "Indonésie d'Alésia Pants",
};

export const sampleWithPartialData: IAdresse = {
  id: 53235,
  departmentName: 'Shoes',
  city: 'Lake Florence',
  stateProvince: 'exuding',
  pays: 'Reduced Buckinghamshire fuchsia',
};

export const sampleWithFullData: IAdresse = {
  id: 22701,
  departmentName: 'programming',
  streetAddress: 'a Ouguiya c',
  postalCode: 'a Assistant',
  city: 'Lake Nestorfort',
  stateProvince: 'b Tunisian Developpeur',
  pays: 'Bonaparte',
};

export const sampleWithNewData: NewAdresse = {
  departmentName: 'payment secured Faubourg',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
