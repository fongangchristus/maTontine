import { IContact, NewContact } from './contact.model';

export const sampleWithRequiredData: IContact = {
  id: 36482,
};

export const sampleWithPartialData: IContact = {
  id: 25447,
  email: 'Guillaume_Cousin96@gmail.com',
};

export const sampleWithFullData: IContact = {
  id: 52265,
  isParDefaut: true,
  email: 'Mence35@gmail.com',
  telephone: '+33 362467329',
  mobile: 'Burundi',
};

export const sampleWithNewData: NewContact = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
