import dayjs from 'dayjs/esm';
import { StatutAdhesion } from 'app/entities/enumerations/statut-adhesion.model';

export interface IAdhesion {
  id: number;
  statutAdhesion?: StatutAdhesion | null;
  matriculePersonne?: string | null;
  dateDebutAdhesion?: dayjs.Dayjs | null;
  dateFinAdhesion?: dayjs.Dayjs | null;
}

export type NewAdhesion = Omit<IAdhesion, 'id'> & { id: null };
