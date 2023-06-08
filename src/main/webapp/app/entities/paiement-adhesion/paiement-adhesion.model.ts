import { IAdhesion } from 'app/entities/adhesion/adhesion.model';

export interface IPaiementAdhesion {
  id: number;
  referencePaiement?: string | null;
  adhesion?: Pick<IAdhesion, 'id'> | null;
}

export type NewPaiementAdhesion = Omit<IPaiementAdhesion, 'id'> & { id: null };
