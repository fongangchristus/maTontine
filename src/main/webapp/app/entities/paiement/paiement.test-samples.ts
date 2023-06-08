import dayjs from 'dayjs/esm';

import { ModePaiement } from 'app/entities/enumerations/mode-paiement.model';
import { StatutPaiement } from 'app/entities/enumerations/statut-paiement.model';

import { IPaiement, NewPaiement } from './paiement.model';

export const sampleWithRequiredData: IPaiement = {
  id: 69996,
  codeAssociation: 'withdrawal Lesotho',
  matriculecmptEmet: 'Chicken programming Loan',
  matriculecmptDest: 'reboot Saint',
  montantPaiement: 41202,
};

export const sampleWithPartialData: IPaiement = {
  id: 969,
  codeAssociation: 'overriding',
  referencePaiement: 'Baby withdrawal data-warehouse',
  matriculecmptEmet: 'seize Unbranded open-source',
  matriculecmptDest: 'c',
  montantPaiement: 97106,
  datePaiement: dayjs('2023-06-07T20:39'),
  modePaiement: ModePaiement['MOBILE_MONEY'],
};

export const sampleWithFullData: IPaiement = {
  id: 48415,
  codeAssociation: 'AGP c',
  referencePaiement: 'Ball Profit-focused',
  matriculecmptEmet: 'initiatives b SMS',
  matriculecmptDest: 'b port Rubber',
  montantPaiement: 78815,
  datePaiement: dayjs('2023-06-07T19:09'),
  modePaiement: ModePaiement['ESPECE'],
  statutPaiement: StatutPaiement['PAYER'],
};

export const sampleWithNewData: NewPaiement = {
  codeAssociation: 'foreground',
  matriculecmptEmet: 'Bacon Home hack',
  matriculecmptDest: 'Cross-group transmitter HTTP',
  montantPaiement: 59143,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
