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

describe('CompteRIB e2e test', () => {
  const compteRIBPageUrl = '/compte-rib';
  const compteRIBPageUrlPattern = new RegExp('/compte-rib(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compteRIBSample = {};

  let compteRIB;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/compte-ribs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compte-ribs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compte-ribs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compteRIB) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/compte-ribs/${compteRIB.id}`,
      }).then(() => {
        compteRIB = undefined;
      });
    }
  });

  it('CompteRIBS menu should load CompteRIBS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compte-rib');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CompteRIB').should('exist');
    cy.url().should('match', compteRIBPageUrlPattern);
  });

  describe('CompteRIB page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(compteRIBPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CompteRIB page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compte-rib/new$'));
        cy.getEntityCreateUpdateHeading('CompteRIB');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteRIBPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/compte-ribs',
          body: compteRIBSample,
        }).then(({ body }) => {
          compteRIB = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/compte-ribs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/compte-ribs?page=0&size=20>; rel="last",<http://localhost/api/compte-ribs?page=0&size=20>; rel="first"',
              },
              body: [compteRIB],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(compteRIBPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CompteRIB page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compteRIB');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteRIBPageUrlPattern);
      });

      it('edit button click should load edit CompteRIB page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteRIB');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteRIBPageUrlPattern);
      });

      it.skip('edit button click should load edit CompteRIB page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteRIB');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteRIBPageUrlPattern);
      });

      it('last delete button click should delete instance of CompteRIB', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('compteRIB').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteRIBPageUrlPattern);

        compteRIB = undefined;
      });
    });
  });

  describe('new CompteRIB page', () => {
    beforeEach(() => {
      cy.visit(`${compteRIBPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CompteRIB');
    });

    it('should create an instance of CompteRIB', () => {
      cy.get(`[data-cy="iban"]`).type('FI6569001500908304').should('have.value', 'FI6569001500908304');

      cy.get(`[data-cy="titulaireCompte"]`).type('global Directeur').should('have.value', 'global Directeur');

      cy.get(`[data-cy="verifier"]`).should('not.be.checked');
      cy.get(`[data-cy="verifier"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        compteRIB = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', compteRIBPageUrlPattern);
    });
  });
});
