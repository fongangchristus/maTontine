import { ICotisationTontine } from 'app/entities/cotisation-tontine/cotisation-tontine.model';
import { IDecaissementTontine } from 'app/entities/decaissement-tontine/decaissement-tontine.model';

export interface IPaiementTontine {
  id: number;
  referencePaiement?: string | null;
  cotisationTontine?: Pick<ICotisationTontine, 'id'> | null;
  decaissementTontine?: Pick<IDecaissementTontine, 'id'> | null;
}

export type NewPaiementTontine = Omit<IPaiementTontine, 'id'> & { id: null };
