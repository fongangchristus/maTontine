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

describe('PaiementAdhesion e2e test', () => {
  const paiementAdhesionPageUrl = '/paiement-adhesion';
  const paiementAdhesionPageUrlPattern = new RegExp('/paiement-adhesion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementAdhesionSample = { referencePaiement: 'Licensed' };

  let paiementAdhesion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paiement-adhesions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paiement-adhesions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paiement-adhesions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementAdhesion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paiement-adhesions/${paiementAdhesion.id}`,
      }).then(() => {
        paiementAdhesion = undefined;
      });
    }
  });

  it('PaiementAdhesions menu should load PaiementAdhesions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paiement-adhesion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementAdhesion').should('exist');
    cy.url().should('match', paiementAdhesionPageUrlPattern);
  });

  describe('PaiementAdhesion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementAdhesionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementAdhesion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paiement-adhesion/new$'));
        cy.getEntityCreateUpdateHeading('PaiementAdhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementAdhesionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paiement-adhesions',
          body: paiementAdhesionSample,
        }).then(({ body }) => {
          paiementAdhesion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paiement-adhesions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/paiement-adhesions?page=0&size=20>; rel="last",<http://localhost/api/paiement-adhesions?page=0&size=20>; rel="first"',
              },
              body: [paiementAdhesion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementAdhesionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementAdhesion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementAdhesion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementAdhesionPageUrlPattern);
      });

      it('edit button click should load edit PaiementAdhesion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementAdhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementAdhesionPageUrlPattern);
      });

      it.skip('edit button click should load edit PaiementAdhesion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementAdhesion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementAdhesionPageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementAdhesion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementAdhesion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementAdhesionPageUrlPattern);

        paiementAdhesion = undefined;
      });
    });
  });

  describe('new PaiementAdhesion page', () => {
    beforeEach(() => {
      cy.visit(`${paiementAdhesionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementAdhesion');
    });

    it('should create an instance of PaiementAdhesion', () => {
      cy.get(`[data-cy="referencePaiement"]`).type('c').should('have.value', 'c');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementAdhesion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementAdhesionPageUrlPattern);
    });
  });
});
