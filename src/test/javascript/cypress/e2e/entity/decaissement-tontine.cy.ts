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

describe('DecaissementTontine e2e test', () => {
  const decaissementTontinePageUrl = '/decaissement-tontine';
  const decaissementTontinePageUrlPattern = new RegExp('/decaissement-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const decaissementTontineSample = {};

  let decaissementTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/decaissement-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/decaissement-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/decaissement-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (decaissementTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/decaissement-tontines/${decaissementTontine.id}`,
      }).then(() => {
        decaissementTontine = undefined;
      });
    }
  });

  it('DecaissementTontines menu should load DecaissementTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('decaissement-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DecaissementTontine').should('exist');
    cy.url().should('match', decaissementTontinePageUrlPattern);
  });

  describe('DecaissementTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(decaissementTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DecaissementTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/decaissement-tontine/new$'));
        cy.getEntityCreateUpdateHeading('DecaissementTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaissementTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/decaissement-tontines',
          body: decaissementTontineSample,
        }).then(({ body }) => {
          decaissementTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/decaissement-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/decaissement-tontines?page=0&size=20>; rel="last",<http://localhost/api/decaissement-tontines?page=0&size=20>; rel="first"',
              },
              body: [decaissementTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(decaissementTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DecaissementTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('decaissementTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaissementTontinePageUrlPattern);
      });

      it('edit button click should load edit DecaissementTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DecaissementTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaissementTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit DecaissementTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DecaissementTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaissementTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of DecaissementTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('decaissementTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', decaissementTontinePageUrlPattern);

        decaissementTontine = undefined;
      });
    });
  });

  describe('new DecaissementTontine page', () => {
    beforeEach(() => {
      cy.visit(`${decaissementTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DecaissementTontine');
    });

    it('should create an instance of DecaissementTontine', () => {
      cy.get(`[data-cy="libele"]`).type('Midi-Pyrénées b').should('have.value', 'Midi-Pyrénées b');

      cy.get(`[data-cy="dateDecaissement"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="montantDecaisse"]`).type('90216').should('have.value', '90216');

      cy.get(`[data-cy="commentaire"]`).type('Malaysian asynchronous Salad').should('have.value', 'Malaysian asynchronous Salad');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        decaissementTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', decaissementTontinePageUrlPattern);
    });
  });
});
