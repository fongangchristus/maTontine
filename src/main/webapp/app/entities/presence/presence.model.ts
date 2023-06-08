import { IFichePresence } from 'app/entities/fiche-presence/fiche-presence.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { StatutPresence } from 'app/entities/enumerations/statut-presence.model';

export interface IPresence {
  id: number;
  matriculeAdherant?: string | null;
  statutPresence?: StatutPresence | null;
  fichePresence?: Pick<IFichePresence, 'id'> | null;
  adherant?: Pick<IPersonne, 'id'> | null;
}

export type NewPresence = Omit<IPresence, 'id'> & { id: null };
