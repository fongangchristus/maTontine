import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContributionPotDetailComponent } from './contribution-pot-detail.component';

describe('ContributionPot Management Detail Component', () => {
  let comp: ContributionPotDetailComponent;
  let fixture: ComponentFixture<ContributionPotDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContributionPotDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contributionPot: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContributionPotDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContributionPotDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contributionPot on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contributionPot).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
