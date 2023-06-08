import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContributionPot, NewContributionPot } from '../contribution-pot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContributionPot for edit and NewContributionPotFormGroupInput for create.
 */
type ContributionPotFormGroupInput = IContributionPot | PartialWithRequiredKeyOf<NewContributionPot>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContributionPot | NewContributionPot> = Omit<T, 'dateContribution'> & {
  dateContribution?: string | null;
};

type ContributionPotFormRawValue = FormValueOf<IContributionPot>;

type NewContributionPotFormRawValue = FormValueOf<NewContributionPot>;

type ContributionPotFormDefaults = Pick<NewContributionPot, 'id' | 'dateContribution'>;

type ContributionPotFormGroupContent = {
  id: FormControl<ContributionPotFormRawValue['id'] | NewContributionPot['id']>;
  identifiant: FormControl<ContributionPotFormRawValue['identifiant']>;
  matriculeContributeur: FormControl<ContributionPotFormRawValue['matriculeContributeur']>;
  montantContribution: FormControl<ContributionPotFormRawValue['montantContribution']>;
  dateContribution: FormControl<ContributionPotFormRawValue['dateContribution']>;
  pot: FormControl<ContributionPotFormRawValue['pot']>;
};

export type ContributionPotFormGroup = FormGroup<ContributionPotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContributionPotFormService {
  createContributionPotFormGroup(contributionPot: ContributionPotFormGroupInput = { id: null }): ContributionPotFormGroup {
    const contributionPotRawValue = this.convertContributionPotToContributionPotRawValue({
      ...this.getFormDefaults(),
      ...contributionPot,
    });
    return new FormGroup<ContributionPotFormGroupContent>({
      id: new FormControl(
        { value: contributionPotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      identifiant: new FormControl(contributionPotRawValue.identifiant),
      matriculeContributeur: new FormControl(contributionPotRawValue.matriculeContributeur, {
        validators: [Validators.required],
      }),
      montantContribution: new FormControl(contributionPotRawValue.montantContribution),
      dateContribution: new FormControl(contributionPotRawValue.dateContribution),
      pot: new FormControl(contributionPotRawValue.pot),
    });
  }

  getContributionPot(form: ContributionPotFormGroup): IContributionPot | NewContributionPot {
    return this.convertContributionPotRawValueToContributionPot(
      form.getRawValue() as ContributionPotFormRawValue | NewContributionPotFormRawValue
    );
  }

  resetForm(form: ContributionPotFormGroup, contributionPot: ContributionPotFormGroupInput): void {
    const contributionPotRawValue = this.convertContributionPotToContributionPotRawValue({ ...this.getFormDefaults(), ...contributionPot });
    form.reset(
      {
        ...contributionPotRawValue,
        id: { value: contributionPotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContributionPotFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateContribution: currentTime,
    };
  }

  private convertContributionPotRawValueToContributionPot(
    rawContributionPot: ContributionPotFormRawValue | NewContributionPotFormRawValue
  ): IContributionPot | NewContributionPot {
    return {
      ...rawContributionPot,
      dateContribution: dayjs(rawContributionPot.dateContribution, DATE_TIME_FORMAT),
    };
  }

  private convertContributionPotToContributionPotRawValue(
    contributionPot: IContributionPot | (Partial<NewContributionPot> & ContributionPotFormDefaults)
  ): ContributionPotFormRawValue | PartialWithRequiredKeyOf<NewContributionPotFormRawValue> {
    return {
      ...contributionPot,
      dateContribution: contributionPot.dateContribution ? contributionPot.dateContribution.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
