import dayjs from 'dayjs/esm';
import { IMonnaie } from 'app/entities/monnaie/monnaie.model';
import { Langue } from 'app/entities/enumerations/langue.model';

export interface IAssociation {
  id: number;
  codeAssociation?: string | null;
  denomination?: string | null;
  slogan?: string | null;
  logoPath?: string | null;
  reglementPath?: string | null;
  statutPath?: string | null;
  description?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  fuseauHoraire?: string | null;
  langue?: Langue | null;
  presentation?: string | null;
  monnaie?: Pick<IMonnaie, 'id'> | null;
}

export type NewAssociation = Omit<IAssociation, 'id'> & { id: null };
