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

describe('SessionTontine e2e test', () => {
  const sessionTontinePageUrl = '/session-tontine';
  const sessionTontinePageUrlPattern = new RegExp('/session-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sessionTontineSample = {};

  let sessionTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/session-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/session-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/session-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sessionTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/session-tontines/${sessionTontine.id}`,
      }).then(() => {
        sessionTontine = undefined;
      });
    }
  });

  it('SessionTontines menu should load SessionTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('session-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SessionTontine').should('exist');
    cy.url().should('match', sessionTontinePageUrlPattern);
  });

  describe('SessionTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sessionTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SessionTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/session-tontine/new$'));
        cy.getEntityCreateUpdateHeading('SessionTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sessionTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/session-tontines',
          body: sessionTontineSample,
        }).then(({ body }) => {
          sessionTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/session-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/session-tontines?page=0&size=20>; rel="last",<http://localhost/api/session-tontines?page=0&size=20>; rel="first"',
              },
              body: [sessionTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sessionTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SessionTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sessionTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sessionTontinePageUrlPattern);
      });

      it('edit button click should load edit SessionTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SessionTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sessionTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit SessionTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SessionTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sessionTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of SessionTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sessionTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sessionTontinePageUrlPattern);

        sessionTontine = undefined;
      });
    });
  });

  describe('new SessionTontine page', () => {
    beforeEach(() => {
      cy.visit(`${sessionTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SessionTontine');
    });

    it('should create an instance of SessionTontine', () => {
      cy.get(`[data-cy="libelle"]`).type('des').should('have.value', 'des');

      cy.get(`[data-cy="dateDebut"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="dateFin"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sessionTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sessionTontinePageUrlPattern);
    });
  });
});
