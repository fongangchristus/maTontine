export interface ITypeEvenement {
  id: number;
  libele?: string | null;
  observation?: string | null;
}

export type NewTypeEvenement = Omit<ITypeEvenement, 'id'> & { id: null };
