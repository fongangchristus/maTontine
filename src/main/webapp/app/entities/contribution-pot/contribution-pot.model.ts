import dayjs from 'dayjs/esm';
import { IPot } from 'app/entities/pot/pot.model';

export interface IContributionPot {
  id: number;
  identifiant?: string | null;
  matriculeContributeur?: string | null;
  montantContribution?: number | null;
  dateContribution?: dayjs.Dayjs | null;
  pot?: Pick<IPot, 'id'> | null;
}

export type NewContributionPot = Omit<IContributionPot, 'id'> & { id: null };
