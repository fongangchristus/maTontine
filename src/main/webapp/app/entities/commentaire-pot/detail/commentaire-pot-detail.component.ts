import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentairePot } from '../commentaire-pot.model';

@Component({
  selector: 'jhi-commentaire-pot-detail',
  templateUrl: './commentaire-pot-detail.component.html',
})
export class CommentairePotDetailComponent implements OnInit {
  commentairePot: ICommentairePot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentairePot }) => {
      this.commentairePot = commentairePot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
