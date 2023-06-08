import dayjs from 'dayjs/esm';
import { ITypePot } from 'app/entities/type-pot/type-pot.model';
import { StatutPot } from 'app/entities/enumerations/statut-pot.model';

export interface IPot {
  id: number;
  libele?: string | null;
  codepot?: string | null;
  montantCible?: number | null;
  description?: string | null;
  dateDebutCollecte?: dayjs.Dayjs | null;
  dateFinCollecte?: dayjs.Dayjs | null;
  statut?: StatutPot | null;
  typePot?: Pick<ITypePot, 'id'> | null;
}

export type NewPot = Omit<IPot, 'id'> & { id: null };
