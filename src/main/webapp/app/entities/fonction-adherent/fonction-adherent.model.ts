import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';
import { IFonction } from 'app/entities/fonction/fonction.model';

export interface IFonctionAdherent {
  id: number;
  matriculeAdherent?: string | null;
  codeFonction?: string | null;
  datePriseFonction?: dayjs.Dayjs | null;
  dateFinFonction?: dayjs.Dayjs | null;
  actif?: boolean | null;
  adherent?: Pick<IPersonne, 'id'> | null;
  fonction?: Pick<IFonction, 'id'> | null;
}

export type NewFonctionAdherent = Omit<IFonctionAdherent, 'id'> & { id: null };
