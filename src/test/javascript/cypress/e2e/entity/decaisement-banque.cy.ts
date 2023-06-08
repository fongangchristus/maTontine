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

describe('DecaisementBanque e2e test', () => {
  const decaisementBanquePageUrl = '/decaisement-banque';
  const decaisementBanquePageUrlPattern = new RegExp('/decaisement-banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const decaisementBanqueSample = {};

  let decaisementBanque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/decaisement-banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/decaisement-banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/decaisement-banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (decaisementBanque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/decaisement-banques/${decaisementBanque.id}`,
      }).then(() => {
        decaisementBanque = undefined;
      });
    }
  });

  it('DecaisementBanques menu should load DecaisementBanques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('decaisement-banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DecaisementBanque').should('exist');
    cy.url().should('match', decaisementBanquePageUrlPattern);
  });

  describe('DecaisementBanque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(decaisementBanquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DecaisementBanque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/decaisement-banque/new$'));
        cy.getEntityCreateUpdateHeading('DecaisementBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaisementBanquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/decaisement-banques',
          body: decaisementBanqueSample,
        }).then(({ body }) => {
          decaisementBanque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/decaisement-banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/decaisement-banques?page=0&size=20>; rel="last",<http://localhost/api/decaisement-banques?page=0&size=20>; rel="first"',
              },
              body: [decaisementBanque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(decaisementBanquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DecaisementBanque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('decaisementBanque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaisementBanquePageUrlPattern);
      });

      it('edit button click should load edit DecaisementBanque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DecaisementBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaisementBanquePageUrlPattern);
      });

      it.skip('edit button click should load edit DecaisementBanque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DecaisementBanque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaisementBanquePageUrlPattern);
      });

      it('last delete button click should delete instance of DecaisementBanque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('decaisementBanque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaisementBanquePageUrlPattern);

        decaisementBanque = undefined;
      });
    });
  });

  describe('new DecaisementBanque page', () => {
    beforeEach(() => {
      cy.visit(`${decaisementBanquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DecaisementBanque');
    });

    it('should create an instance of DecaisementBanque', () => {
      cy.get(`[data-cy="libelle"]`).type('magenta deposit Manager').should('have.value', 'magenta deposit Manager');

      cy.get(`[data-cy="montant"]`).type('40539').should('have.value', '40539');

      cy.get(`[data-cy="dateDecaissement"]`).type('2023-06-08T09:05').blur().should('have.value', '2023-06-08T09:05');

      cy.get(`[data-cy="montantDecaisse"]`).type('69731').should('have.value', '69731');

      cy.get(`[data-cy="commentaire"]`).type('Books').should('have.value', 'Books');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        decaisementBanque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', decaisementBanquePageUrlPattern);
    });
  });
});
