import { IBanque } from 'app/entities/banque/banque.model';

export interface IGestionnaireBanque {
  id: number;
  matriculeMembre?: string | null;
  banque?: Pick<IBanque, 'id'> | null;
}

export type NewGestionnaireBanque = Omit<IGestionnaireBanque, 'id'> & { id: null };
