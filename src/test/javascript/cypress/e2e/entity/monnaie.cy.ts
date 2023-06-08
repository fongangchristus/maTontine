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

describe('Monnaie e2e test', () => {
  const monnaiePageUrl = '/monnaie';
  const monnaiePageUrlPattern = new RegExp('/monnaie(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const monnaieSample = {};

  let monnaie;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/monnaies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/monnaies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/monnaies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (monnaie) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/monnaies/${monnaie.id}`,
      }).then(() => {
        monnaie = undefined;
      });
    }
  });

  it('Monnaies menu should load Monnaies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('monnaie');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Monnaie').should('exist');
    cy.url().should('match', monnaiePageUrlPattern);
  });

  describe('Monnaie page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(monnaiePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Monnaie page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/monnaie/new$'));
        cy.getEntityCreateUpdateHeading('Monnaie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', monnaiePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/monnaies',
          body: monnaieSample,
        }).then(({ body }) => {
          monnaie = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/monnaies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/monnaies?page=0&size=20>; rel="last",<http://localhost/api/monnaies?page=0&size=20>; rel="first"',
              },
              body: [monnaie],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(monnaiePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Monnaie page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('monnaie');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', monnaiePageUrlPattern);
      });

      it('edit button click should load edit Monnaie page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Monnaie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', monnaiePageUrlPattern);
      });

      it.skip('edit button click should load edit Monnaie page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Monnaie');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', monnaiePageUrlPattern);
      });

      it('last delete button click should delete instance of Monnaie', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('monnaie').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', monnaiePageUrlPattern);

        monnaie = undefined;
      });
    });
  });

  describe('new Monnaie page', () => {
    beforeEach(() => {
      cy.visit(`${monnaiePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Monnaie');
    });

    it('should create an instance of Monnaie', () => {
      cy.get(`[data-cy="libele"]`).type('Saint-Bernard Delesseux Checking').should('have.value', 'Saint-Bernard Delesseux Checking');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        monnaie = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', monnaiePageUrlPattern);
    });
  });
});
