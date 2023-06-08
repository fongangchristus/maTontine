import { IBanque } from 'app/entities/banque/banque.model';

export interface ICompteBanque {
  id: number;
  libelle?: string | null;
  description?: string | null;
  matriculeAdherant?: string | null;
  montantDisponnible?: number | null;
  banque?: Pick<IBanque, 'id'> | null;
}

export type NewCompteBanque = Omit<ICompteBanque, 'id'> & { id: null };
