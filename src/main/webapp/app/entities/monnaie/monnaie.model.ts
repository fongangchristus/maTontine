export interface IMonnaie {
  id: number;
  libele?: string | null;
}

export type NewMonnaie = Omit<IMonnaie, 'id'> & { id: null };
