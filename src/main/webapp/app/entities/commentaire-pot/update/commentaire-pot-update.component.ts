import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommentairePotFormService, CommentairePotFormGroup } from './commentaire-pot-form.service';
import { ICommentairePot } from '../commentaire-pot.model';
import { CommentairePotService } from '../service/commentaire-pot.service';
import { IPot } from 'app/entities/pot/pot.model';
import { PotService } from 'app/entities/pot/service/pot.service';

@Component({
  selector: 'jhi-commentaire-pot-update',
  templateUrl: './commentaire-pot-update.component.html',
})
export class CommentairePotUpdateComponent implements OnInit {
  isSaving = false;
  commentairePot: ICommentairePot | null = null;

  potsSharedCollection: IPot[] = [];

  editForm: CommentairePotFormGroup = this.commentairePotFormService.createCommentairePotFormGroup();

  constructor(
    protected commentairePotService: CommentairePotService,
    protected commentairePotFormService: CommentairePotFormService,
    protected potService: PotService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePot = (o1: IPot | null, o2: IPot | null): boolean => this.potService.comparePot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentairePot }) => {
      this.commentairePot = commentairePot;
      if (commentairePot) {
        this.updateForm(commentairePot);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commentairePot = this.commentairePotFormService.getCommentairePot(this.editForm);
    if (commentairePot.id !== null) {
      this.subscribeToSaveResponse(this.commentairePotService.update(commentairePot));
    } else {
      this.subscribeToSaveResponse(this.commentairePotService.create(commentairePot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentairePot>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commentairePot: ICommentairePot): void {
    this.commentairePot = commentairePot;
    this.commentairePotFormService.resetForm(this.editForm, commentairePot);

    this.potsSharedCollection = this.potService.addPotToCollectionIfMissing<IPot>(this.potsSharedCollection, commentairePot.pot);
  }

  protected loadRelationshipsOptions(): void {
    this.potService
      .query()
      .pipe(map((res: HttpResponse<IPot[]>) => res.body ?? []))
      .pipe(map((pots: IPot[]) => this.potService.addPotToCollectionIfMissing<IPot>(pots, this.commentairePot?.pot)))
      .subscribe((pots: IPot[]) => (this.potsSharedCollection = pots));
  }
}
