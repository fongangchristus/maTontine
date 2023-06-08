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

describe('Fonction e2e test', () => {
  const fonctionPageUrl = '/fonction';
  const fonctionPageUrlPattern = new RegExp('/fonction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fonctionSample = {};

  let fonction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fonctions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fonctions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fonctions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fonction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fonctions/${fonction.id}`,
      }).then(() => {
        fonction = undefined;
      });
    }
  });

  it('Fonctions menu should load Fonctions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fonction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fonction').should('exist');
    cy.url().should('match', fonctionPageUrlPattern);
  });

  describe('Fonction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fonctionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fonction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fonction/new$'));
        cy.getEntityCreateUpdateHeading('Fonction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fonctions',
          body: fonctionSample,
        }).then(({ body }) => {
          fonction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fonctions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fonctions?page=0&size=20>; rel="last",<http://localhost/api/fonctions?page=0&size=20>; rel="first"',
              },
              body: [fonction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fonctionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fonction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fonction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionPageUrlPattern);
      });

      it('edit button click should load edit Fonction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fonction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionPageUrlPattern);
      });

      it.skip('edit button click should load edit Fonction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fonction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionPageUrlPattern);
      });

      it('last delete button click should delete instance of Fonction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fonction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionPageUrlPattern);

        fonction = undefined;
      });
    });
  });

  describe('new Fonction page', () => {
    beforeEach(() => {
      cy.visit(`${fonctionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fonction');
    });

    it('should create an instance of Fonction', () => {
      cy.get(`[data-cy="title"]`).type('Concrete').should('have.value', 'Concrete');

      cy.get(`[data-cy="description"]`).type('actuating bypass infrastructure').should('have.value', 'actuating bypass infrastructure');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fonction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fonctionPageUrlPattern);
    });
  });
});
