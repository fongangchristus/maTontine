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

describe('Adhesion e2e test', () => {
  const adhesionPageUrl = '/adhesion';
  const adhesionPageUrlPattern = new RegExp('/adhesion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adhesionSample = {};

  let adhesion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/adhesions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/adhesions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/adhesions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (adhesion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/adhesions/${adhesion.id}`,
      }).then(() => {
        adhesion = undefined;
      });
    }
  });

  it('Adhesions menu should load Adhesions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('adhesion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Adhesion').should('exist');
    cy.url().should('match', adhesionPageUrlPattern);
  });

  describe('Adhesion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adhesionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Adhesion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/adhesion/new$'));
        cy.getEntityCreateUpdateHeading('Adhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adhesionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/adhesions',
          body: adhesionSample,
        }).then(({ body }) => {
          adhesion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/adhesions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/adhesions?page=0&size=20>; rel="last",<http://localhost/api/adhesions?page=0&size=20>; rel="first"',
              },
              body: [adhesion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(adhesionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Adhesion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adhesion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adhesionPageUrlPattern);
      });

      it('edit button click should load edit Adhesion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Adhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adhesionPageUrlPattern);
      });

      it.skip('edit button click should load edit Adhesion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Adhesion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adhesionPageUrlPattern);
      });

      it('last delete button click should delete instance of Adhesion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('adhesion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adhesionPageUrlPattern);

        adhesion = undefined;
      });
    });
  });

  describe('new Adhesion page', () => {
    beforeEach(() => {
      cy.visit(`${adhesionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Adhesion');
    });

    it('should create an instance of Adhesion', () => {
      cy.get(`[data-cy="statutAdhesion"]`).select('DESACTIVE');

      cy.get(`[data-cy="matriculePersonne"]`).type('Architecte a Sports').should('have.value', 'Architecte a Sports');

      cy.get(`[data-cy="dateDebutAdhesion"]`).type('2023-06-07T16:10').blur().should('have.value', '2023-06-07T16:10');

      cy.get(`[data-cy="dateFinAdhesion"]`).type('2023-06-07T21:56').blur().should('have.value', '2023-06-07T21:56');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        adhesion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adhesionPageUrlPattern);
    });
  });
});
