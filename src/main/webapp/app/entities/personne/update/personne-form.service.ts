import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonne, NewPersonne } from '../personne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonne for edit and NewPersonneFormGroupInput for create.
 */
type PersonneFormGroupInput = IPersonne | PartialWithRequiredKeyOf<NewPersonne>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPersonne | NewPersonne> = Omit<T, 'dateInscription' | 'dateIntegration'> & {
  dateInscription?: string | null;
  dateIntegration?: string | null;
};

type PersonneFormRawValue = FormValueOf<IPersonne>;

type NewPersonneFormRawValue = FormValueOf<NewPersonne>;

type PersonneFormDefaults = Pick<NewPersonne, 'id' | 'dateInscription' | 'dateIntegration' | 'isAdmin' | 'isDonateur' | 'isBenevole'>;

type PersonneFormGroupContent = {
  id: FormControl<PersonneFormRawValue['id'] | NewPersonne['id']>;
  idUser: FormControl<PersonneFormRawValue['idUser']>;
  codeAssociation: FormControl<PersonneFormRawValue['codeAssociation']>;
  matricule: FormControl<PersonneFormRawValue['matricule']>;
  nom: FormControl<PersonneFormRawValue['nom']>;
  prenom: FormControl<PersonneFormRawValue['prenom']>;
  telephone: FormControl<PersonneFormRawValue['telephone']>;
  email: FormControl<PersonneFormRawValue['email']>;
  dateNaissance: FormControl<PersonneFormRawValue['dateNaissance']>;
  lieuNaissance: FormControl<PersonneFormRawValue['lieuNaissance']>;
  dateInscription: FormControl<PersonneFormRawValue['dateInscription']>;
  profession: FormControl<PersonneFormRawValue['profession']>;
  sexe: FormControl<PersonneFormRawValue['sexe']>;
  photoPath: FormControl<PersonneFormRawValue['photoPath']>;
  dateIntegration: FormControl<PersonneFormRawValue['dateIntegration']>;
  isAdmin: FormControl<PersonneFormRawValue['isAdmin']>;
  isDonateur: FormControl<PersonneFormRawValue['isDonateur']>;
  isBenevole: FormControl<PersonneFormRawValue['isBenevole']>;
  typePersonne: FormControl<PersonneFormRawValue['typePersonne']>;
  adresse: FormControl<PersonneFormRawValue['adresse']>;
};

export type PersonneFormGroup = FormGroup<PersonneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonneFormService {
  createPersonneFormGroup(personne: PersonneFormGroupInput = { id: null }): PersonneFormGroup {
    const personneRawValue = this.convertPersonneToPersonneRawValue({
      ...this.getFormDefaults(),
      ...personne,
    });
    return new FormGroup<PersonneFormGroupContent>({
      id: new FormControl(
        { value: personneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idUser: new FormControl(personneRawValue.idUser),
      codeAssociation: new FormControl(personneRawValue.codeAssociation),
      matricule: new FormControl(personneRawValue.matricule),
      nom: new FormControl(personneRawValue.nom),
      prenom: new FormControl(personneRawValue.prenom),
      telephone: new FormControl(personneRawValue.telephone, {
        validators: [Validators.required],
      }),
      email: new FormControl(personneRawValue.email),
      dateNaissance: new FormControl(personneRawValue.dateNaissance),
      lieuNaissance: new FormControl(personneRawValue.lieuNaissance),
      dateInscription: new FormControl(personneRawValue.dateInscription),
      profession: new FormControl(personneRawValue.profession),
      sexe: new FormControl(personneRawValue.sexe),
      photoPath: new FormControl(personneRawValue.photoPath),
      dateIntegration: new FormControl(personneRawValue.dateIntegration),
      isAdmin: new FormControl(personneRawValue.isAdmin),
      isDonateur: new FormControl(personneRawValue.isDonateur),
      isBenevole: new FormControl(personneRawValue.isBenevole),
      typePersonne: new FormControl(personneRawValue.typePersonne),
      adresse: new FormControl(personneRawValue.adresse),
    });
  }

  getPersonne(form: PersonneFormGroup): IPersonne | NewPersonne {
    return this.convertPersonneRawValueToPersonne(form.getRawValue() as PersonneFormRawValue | NewPersonneFormRawValue);
  }

  resetForm(form: PersonneFormGroup, personne: PersonneFormGroupInput): void {
    const personneRawValue = this.convertPersonneToPersonneRawValue({ ...this.getFormDefaults(), ...personne });
    form.reset(
      {
        ...personneRawValue,
        id: { value: personneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PersonneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateInscription: currentTime,
      dateIntegration: currentTime,
      isAdmin: false,
      isDonateur: false,
      isBenevole: false,
    };
  }

  private convertPersonneRawValueToPersonne(rawPersonne: PersonneFormRawValue | NewPersonneFormRawValue): IPersonne | NewPersonne {
    return {
      ...rawPersonne,
      dateInscription: dayjs(rawPersonne.dateInscription, DATE_TIME_FORMAT),
      dateIntegration: dayjs(rawPersonne.dateIntegration, DATE_TIME_FORMAT),
    };
  }

  private convertPersonneToPersonneRawValue(
    personne: IPersonne | (Partial<NewPersonne> & PersonneFormDefaults)
  ): PersonneFormRawValue | PartialWithRequiredKeyOf<NewPersonneFormRawValue> {
    return {
      ...personne,
      dateInscription: personne.dateInscription ? personne.dateInscription.format(DATE_TIME_FORMAT) : undefined,
      dateIntegration: personne.dateIntegration ? personne.dateIntegration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
