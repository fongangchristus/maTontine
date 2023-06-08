import dayjs from 'dayjs/esm';
import { IAdhesion } from 'app/entities/adhesion/adhesion.model';

export interface IFormuleAdhesion {
  id: number;
  adhesionPeriodique?: boolean | null;
  dateDebut?: dayjs.Dayjs | null;
  dureeAdhesionMois?: number | null;
  montantLibre?: boolean | null;
  description?: string | null;
  tarif?: number | null;
  adhesion?: Pick<IAdhesion, 'id'> | null;
}

export type NewFormuleAdhesion = Omit<IFormuleAdhesion, 'id'> & { id: null };
