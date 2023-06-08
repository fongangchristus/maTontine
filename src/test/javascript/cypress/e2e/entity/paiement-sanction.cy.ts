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

describe('PaiementSanction e2e test', () => {
  const paiementSanctionPageUrl = '/paiement-sanction';
  const paiementSanctionPageUrlPattern = new RegExp('/paiement-sanction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementSanctionSample = { referencePaiement: 'unleash de supply-chains' };

  let paiementSanction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paiement-sanctions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paiement-sanctions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paiement-sanctions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementSanction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paiement-sanctions/${paiementSanction.id}`,
      }).then(() => {
        paiementSanction = undefined;
      });
    }
  });

  it('PaiementSanctions menu should load PaiementSanctions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paiement-sanction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementSanction').should('exist');
    cy.url().should('match', paiementSanctionPageUrlPattern);
  });

  describe('PaiementSanction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementSanctionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementSanction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paiement-sanction/new$'));
        cy.getEntityCreateUpdateHeading('PaiementSanction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementSanctionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paiement-sanctions',
          body: paiementSanctionSample,
        }).then(({ body }) => {
          paiementSanction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paiement-sanctions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/paiement-sanctions?page=0&size=20>; rel="last",<http://localhost/api/paiement-sanctions?page=0&size=20>; rel="first"',
              },
              body: [paiementSanction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementSanctionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementSanction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementSanction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementSanctionPageUrlPattern);
      });

      it('edit button click should load edit PaiementSanction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementSanction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementSanctionPageUrlPattern);
      });

      it.skip('edit button click should load edit PaiementSanction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementSanction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementSanctionPageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementSanction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementSanction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementSanctionPageUrlPattern);

        paiementSanction = undefined;
      });
    });
  });

  describe('new PaiementSanction page', () => {
    beforeEach(() => {
      cy.visit(`${paiementSanctionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementSanction');
    });

    it('should create an instance of PaiementSanction', () => {
      cy.get(`[data-cy="referencePaiement"]`).type('Borders Incredible').should('have.value', 'Borders Incredible');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementSanction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementSanctionPageUrlPattern);
    });
  });
});
