jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FormuleAdhesionService } from '../service/formule-adhesion.service';

import { FormuleAdhesionDeleteDialogComponent } from './formule-adhesion-delete-dialog.component';

describe('FormuleAdhesion Management Delete Component', () => {
  let comp: FormuleAdhesionDeleteDialogComponent;
  let fixture: ComponentFixture<FormuleAdhesionDeleteDialogComponent>;
  let service: FormuleAdhesionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormuleAdhesionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(FormuleAdhesionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormuleAdhesionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FormuleAdhesionService);
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
