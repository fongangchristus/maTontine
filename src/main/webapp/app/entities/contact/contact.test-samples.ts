import { IContact, NewContact } from './contact.model';

export const sampleWithRequiredData: IContact = {
  id: 36482,
};

export const sampleWithPartialData: IContact = {
  id: 20106,
  telephone: '0407988958',
};

export const sampleWithFullData: IContact = {
  id: 3657,
  email: 'Anne_Lopez@hotmail.fr',
  telephone: '+33 324673290',
  mobile: 'Granite productize',
};

export const sampleWithNewData: NewContact = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
