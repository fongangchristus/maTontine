import dayjs from 'dayjs/esm';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';

export interface ICotisationBanque {
  id: number;
  libelle?: string | null;
  montant?: number | null;
  dateCotisation?: dayjs.Dayjs | null;
  montantCotise?: number | null;
  commentaire?: string | null;
  compteBanque?: Pick<ICompteBanque, 'id'> | null;
}

export type NewCotisationBanque = Omit<ICotisationBanque, 'id'> & { id: null };
