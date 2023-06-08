import { TypeSanction } from 'app/entities/enumerations/type-sanction.model';

import { ISanctionConfiguration, NewSanctionConfiguration } from './sanction-configuration.model';

export const sampleWithRequiredData: ISanctionConfiguration = {
  id: 217,
  codeAssociation: 'PNG Unit',
  codeTontine: 'Frozen extensible',
};

export const sampleWithPartialData: ISanctionConfiguration = {
  id: 82334,
  codeAssociation: 'Card redundant',
  codeTontine: 'transmitting Industrial input',
  type: TypeSanction['SANCTION_ASSEMBLE'],
};

export const sampleWithFullData: ISanctionConfiguration = {
  id: 77899,
  codeAssociation: 'b Ergonomic knowledge',
  codeTontine: 'virtual RAM Incredible',
  type: TypeSanction['RETARD_PRESENCE'],
};

export const sampleWithNewData: NewSanctionConfiguration = {
  codeAssociation: 'Pizza',
  codeTontine: 'plug-and-play',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
