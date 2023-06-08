jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SanctionConfigurationService } from '../service/sanction-configuration.service';

import { SanctionConfigurationDeleteDialogComponent } from './sanction-configuration-delete-dialog.component';

describe('SanctionConfiguration Management Delete Component', () => {
  let comp: SanctionConfigurationDeleteDialogComponent;
  let fixture: ComponentFixture<SanctionConfigurationDeleteDialogComponent>;
  let service: SanctionConfigurationService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SanctionConfigurationDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SanctionConfigurationDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SanctionConfigurationDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SanctionConfigurationService);
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
