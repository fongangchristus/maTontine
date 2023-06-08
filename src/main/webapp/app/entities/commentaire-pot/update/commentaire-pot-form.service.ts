import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommentairePot, NewCommentairePot } from '../commentaire-pot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommentairePot for edit and NewCommentairePotFormGroupInput for create.
 */
type CommentairePotFormGroupInput = ICommentairePot | PartialWithRequiredKeyOf<NewCommentairePot>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICommentairePot | NewCommentairePot> = Omit<T, 'dateComentaire'> & {
  dateComentaire?: string | null;
};

type CommentairePotFormRawValue = FormValueOf<ICommentairePot>;

type NewCommentairePotFormRawValue = FormValueOf<NewCommentairePot>;

type CommentairePotFormDefaults = Pick<NewCommentairePot, 'id' | 'dateComentaire'>;

type CommentairePotFormGroupContent = {
  id: FormControl<CommentairePotFormRawValue['id'] | NewCommentairePot['id']>;
  matriculeContributeur: FormControl<CommentairePotFormRawValue['matriculeContributeur']>;
  identifiantPot: FormControl<CommentairePotFormRawValue['identifiantPot']>;
  contenu: FormControl<CommentairePotFormRawValue['contenu']>;
  dateComentaire: FormControl<CommentairePotFormRawValue['dateComentaire']>;
  pot: FormControl<CommentairePotFormRawValue['pot']>;
};

export type CommentairePotFormGroup = FormGroup<CommentairePotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommentairePotFormService {
  createCommentairePotFormGroup(commentairePot: CommentairePotFormGroupInput = { id: null }): CommentairePotFormGroup {
    const commentairePotRawValue = this.convertCommentairePotToCommentairePotRawValue({
      ...this.getFormDefaults(),
      ...commentairePot,
    });
    return new FormGroup<CommentairePotFormGroupContent>({
      id: new FormControl(
        { value: commentairePotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matriculeContributeur: new FormControl(commentairePotRawValue.matriculeContributeur, {
        validators: [Validators.required],
      }),
      identifiantPot: new FormControl(commentairePotRawValue.identifiantPot, {
        validators: [Validators.required],
      }),
      contenu: new FormControl(commentairePotRawValue.contenu),
      dateComentaire: new FormControl(commentairePotRawValue.dateComentaire),
      pot: new FormControl(commentairePotRawValue.pot),
    });
  }

  getCommentairePot(form: CommentairePotFormGroup): ICommentairePot | NewCommentairePot {
    return this.convertCommentairePotRawValueToCommentairePot(
      form.getRawValue() as CommentairePotFormRawValue | NewCommentairePotFormRawValue
    );
  }

  resetForm(form: CommentairePotFormGroup, commentairePot: CommentairePotFormGroupInput): void {
    const commentairePotRawValue = this.convertCommentairePotToCommentairePotRawValue({ ...this.getFormDefaults(), ...commentairePot });
    form.reset(
      {
        ...commentairePotRawValue,
        id: { value: commentairePotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommentairePotFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateComentaire: currentTime,
    };
  }

  private convertCommentairePotRawValueToCommentairePot(
    rawCommentairePot: CommentairePotFormRawValue | NewCommentairePotFormRawValue
  ): ICommentairePot | NewCommentairePot {
    return {
      ...rawCommentairePot,
      dateComentaire: dayjs(rawCommentairePot.dateComentaire, DATE_TIME_FORMAT),
    };
  }

  private convertCommentairePotToCommentairePotRawValue(
    commentairePot: ICommentairePot | (Partial<NewCommentairePot> & CommentairePotFormDefaults)
  ): CommentairePotFormRawValue | PartialWithRequiredKeyOf<NewCommentairePotFormRawValue> {
    return {
      ...commentairePot,
      dateComentaire: commentairePot.dateComentaire ? commentairePot.dateComentaire.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
