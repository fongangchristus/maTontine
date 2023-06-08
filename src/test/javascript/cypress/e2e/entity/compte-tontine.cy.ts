import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('CompteTontine e2e test', () => {
  const compteTontinePageUrl = '/compte-tontine';
  const compteTontinePageUrlPattern = new RegExp('/compte-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compteTontineSample = { matriculeCompte: 'transitional motivating', matriculeMenbre: 'Lithuanian navigating overriding' };

  let compteTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/compte-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compte-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compte-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compteTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/compte-tontines/${compteTontine.id}`,
      }).then(() => {
        compteTontine = undefined;
      });
    }
  });

  it('CompteTontines menu should load CompteTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compte-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CompteTontine').should('exist');
    cy.url().should('match', compteTontinePageUrlPattern);
  });

  describe('CompteTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(compteTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CompteTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compte-tontine/new$'));
        cy.getEntityCreateUpdateHeading('CompteTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/compte-tontines',
          body: compteTontineSample,
        }).then(({ body }) => {
          compteTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/compte-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/compte-tontines?page=0&size=20>; rel="last",<http://localhost/api/compte-tontines?page=0&size=20>; rel="first"',
              },
              body: [compteTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(compteTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CompteTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compteTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteTontinePageUrlPattern);
      });

      it('edit button click should load edit CompteTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit CompteTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of CompteTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('compteTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteTontinePageUrlPattern);

        compteTontine = undefined;
      });
    });
  });

  describe('new CompteTontine page', () => {
    beforeEach(() => {
      cy.visit(`${compteTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CompteTontine');
    });

    it('should create an instance of CompteTontine', () => {
      cy.get(`[data-cy="etatDeCompte"]`).should('not.be.checked');
      cy.get(`[data-cy="etatDeCompte"]`).click().should('be.checked');

      cy.get(`[data-cy="libele"]`).type('Delesseux').should('have.value', 'Delesseux');

      cy.get(`[data-cy="odreBeneficiere"]`).type('65379').should('have.value', '65379');

      cy.get(`[data-cy="matriculeCompte"]`).type('Krone FTP teal').should('have.value', 'Krone FTP teal');

      cy.get(`[data-cy="matriculeMenbre"]`).type('Computers unleash e-markets').should('have.value', 'Computers unleash e-markets');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        compteTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', compteTontinePageUrlPattern);
    });
  });
});
