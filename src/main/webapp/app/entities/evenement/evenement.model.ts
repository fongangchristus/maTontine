import dayjs from 'dayjs/esm';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';

export interface IEvenement {
  id: number;
  libele?: string | null;
  codepot?: string | null;
  montantPayer?: string | null;
  description?: string | null;
  budget?: number | null;
  dateEvenement?: dayjs.Dayjs | null;
  typeEvenement?: Pick<ITypeEvenement, 'id'> | null;
}

export type NewEvenement = Omit<IEvenement, 'id'> & { id: null };
