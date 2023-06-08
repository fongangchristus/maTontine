import { IPersonne } from 'app/entities/personne/personne.model';

export interface ICompteRIB {
  id: number;
  iban?: string | null;
  titulaireCompte?: string | null;
  verifier?: boolean | null;
  adherent?: Pick<IPersonne, 'id'> | null;
}

export type NewCompteRIB = Omit<ICompteRIB, 'id'> & { id: null };
