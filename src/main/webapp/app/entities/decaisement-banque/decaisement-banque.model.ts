import dayjs from 'dayjs/esm';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';

export interface IDecaisementBanque {
  id: number;
  libelle?: string | null;
  montant?: number | null;
  dateDecaissement?: dayjs.Dayjs | null;
  montantDecaisse?: number | null;
  commentaire?: string | null;
  compteBanque?: Pick<ICompteBanque, 'id'> | null;
}

export type NewDecaisementBanque = Omit<IDecaisementBanque, 'id'> & { id: null };
