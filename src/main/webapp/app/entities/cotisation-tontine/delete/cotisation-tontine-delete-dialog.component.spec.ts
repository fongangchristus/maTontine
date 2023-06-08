jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CotisationTontineService } from '../service/cotisation-tontine.service';

import { CotisationTontineDeleteDialogComponent } from './cotisation-tontine-delete-dialog.component';

describe('CotisationTontine Management Delete Component', () => {
  let comp: CotisationTontineDeleteDialogComponent;
  let fixture: ComponentFixture<CotisationTontineDeleteDialogComponent>;
  let service: CotisationTontineService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CotisationTontineDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CotisationTontineDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CotisationTontineDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CotisationTontineService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
