export interface IPaiementBanque {
  id: number;
  referencePaiement?: string | null;
}

export type NewPaiementBanque = Omit<IPaiementBanque, 'id'> & { id: null };
