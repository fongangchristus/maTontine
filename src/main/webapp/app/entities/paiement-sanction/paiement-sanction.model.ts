import { ISanction } from 'app/entities/sanction/sanction.model';

export interface IPaiementSanction {
  id: number;
  referencePaiement?: string | null;
  sanction?: Pick<ISanction, 'id'> | null;
}

export type NewPaiementSanction = Omit<IPaiementSanction, 'id'> & { id: null };
