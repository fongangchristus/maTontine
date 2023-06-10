import { IPersonne } from 'app/entities/personne/personne.model';

export interface IContact {
  id: number;
  isParDefaut?: boolean | null;
  email?: string | null;
  telephone?: string | null;
  mobile?: string | null;
  adherent?: Pick<IPersonne, 'id'> | null;
}

export type NewContact = Omit<IContact, 'id'> & { id: null };
