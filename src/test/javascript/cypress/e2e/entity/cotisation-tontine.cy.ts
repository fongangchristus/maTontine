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

describe('CotisationTontine e2e test', () => {
  const cotisationTontinePageUrl = '/cotisation-tontine';
  const cotisationTontinePageUrlPattern = new RegExp('/cotisation-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cotisationTontineSample = {};

  let cotisationTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cotisation-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cotisation-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cotisation-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cotisationTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cotisation-tontines/${cotisationTontine.id}`,
      }).then(() => {
        cotisationTontine = undefined;
      });
    }
  });

  it('CotisationTontines menu should load CotisationTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cotisation-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CotisationTontine').should('exist');
    cy.url().should('match', cotisationTontinePageUrlPattern);
  });

  describe('CotisationTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cotisationTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CotisationTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cotisation-tontine/new$'));
        cy.getEntityCreateUpdateHeading('CotisationTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cotisation-tontines',
          body: cotisationTontineSample,
        }).then(({ body }) => {
          cotisationTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cotisation-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cotisation-tontines?page=0&size=20>; rel="last",<http://localhost/api/cotisation-tontines?page=0&size=20>; rel="first"',
              },
              body: [cotisationTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cotisationTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CotisationTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cotisationTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationTontinePageUrlPattern);
      });

      it('edit button click should load edit CotisationTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CotisationTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit CotisationTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CotisationTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of CotisationTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cotisationTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationTontinePageUrlPattern);

        cotisationTontine = undefined;
      });
    });
  });

  describe('new CotisationTontine page', () => {
    beforeEach(() => {
      cy.visit(`${cotisationTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CotisationTontine');
    });

    it('should create an instance of CotisationTontine', () => {
      cy.get(`[data-cy="montantCotise"]`).type('11898').should('have.value', '11898');

      cy.get(`[data-cy="pieceJustifPath"]`).type('Fantastic').should('have.value', 'Fantastic');

      cy.get(`[data-cy="dateCotisation"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="dateValidation"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="commentaire"]`).type('SQL').should('have.value', 'SQL');

      cy.get(`[data-cy="etat"]`).select('ENCOURS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cotisationTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cotisationTontinePageUrlPattern);
    });
  });
});
