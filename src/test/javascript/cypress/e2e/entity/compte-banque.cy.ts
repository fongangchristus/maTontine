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

describe('CompteBanque e2e test', () => {
  const compteBanquePageUrl = '/compte-banque';
  const compteBanquePageUrlPattern = new RegExp('/compte-banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compteBanqueSample = {};

  let compteBanque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/compte-banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compte-banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compte-banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compteBanque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/compte-banques/${compteBanque.id}`,
      }).then(() => {
        compteBanque = undefined;
      });
    }
  });

  it('CompteBanques menu should load CompteBanques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compte-banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CompteBanque').should('exist');
    cy.url().should('match', compteBanquePageUrlPattern);
  });

  describe('CompteBanque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(compteBanquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CompteBanque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compte-banque/new$'));
        cy.getEntityCreateUpdateHeading('CompteBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteBanquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/compte-banques',
          body: compteBanqueSample,
        }).then(({ body }) => {
          compteBanque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/compte-banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/compte-banques?page=0&size=20>; rel="last",<http://localhost/api/compte-banques?page=0&size=20>; rel="first"',
              },
              body: [compteBanque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(compteBanquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CompteBanque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compteBanque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteBanquePageUrlPattern);
      });

      it('edit button click should load edit CompteBanque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteBanquePageUrlPattern);
      });

      it.skip('edit button click should load edit CompteBanque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompteBanque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteBanquePageUrlPattern);
      });

      it('last delete button click should delete instance of CompteBanque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('compteBanque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compteBanquePageUrlPattern);

        compteBanque = undefined;
      });
    });
  });

  describe('new CompteBanque page', () => {
    beforeEach(() => {
      cy.visit(`${compteBanquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CompteBanque');
    });

    it('should create an instance of CompteBanque', () => {
      cy.get(`[data-cy="libelle"]`).type('Rubber Grocery Account').should('have.value', 'Rubber Grocery Account');

      cy.get(`[data-cy="description"]`).type('Silver').should('have.value', 'Silver');

      cy.get(`[data-cy="matriculeAdherant"]`)
        .type('Bolivie holistic Buckinghamshire')
        .should('have.value', 'Bolivie holistic Buckinghamshire');

      cy.get(`[data-cy="montantDisponnible"]`).type('33545').should('have.value', '33545');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        compteBanque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', compteBanquePageUrlPattern);
    });
  });
});
