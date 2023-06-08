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

describe('PaiementTontine e2e test', () => {
  const paiementTontinePageUrl = '/paiement-tontine';
  const paiementTontinePageUrlPattern = new RegExp('/paiement-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementTontineSample = {};

  let paiementTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paiement-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paiement-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paiement-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paiement-tontines/${paiementTontine.id}`,
      }).then(() => {
        paiementTontine = undefined;
      });
    }
  });

  it('PaiementTontines menu should load PaiementTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paiement-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementTontine').should('exist');
    cy.url().should('match', paiementTontinePageUrlPattern);
  });

  describe('PaiementTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paiement-tontine/new$'));
        cy.getEntityCreateUpdateHeading('PaiementTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paiement-tontines',
          body: paiementTontineSample,
        }).then(({ body }) => {
          paiementTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paiement-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/paiement-tontines?page=0&size=20>; rel="last",<http://localhost/api/paiement-tontines?page=0&size=20>; rel="first"',
              },
              body: [paiementTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementTontinePageUrlPattern);
      });

      it('edit button click should load edit PaiementTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit PaiementTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementTontinePageUrlPattern);

        paiementTontine = undefined;
      });
    });
  });

  describe('new PaiementTontine page', () => {
    beforeEach(() => {
      cy.visit(`${paiementTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementTontine');
    });

    it('should create an instance of PaiementTontine', () => {
      cy.get(`[data-cy="referencePaiement"]`).type('Garden payment').should('have.value', 'Garden payment');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementTontinePageUrlPattern);
    });
  });
});
