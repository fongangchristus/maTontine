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

describe('Tontine e2e test', () => {
  const tontinePageUrl = '/tontine';
  const tontinePageUrlPattern = new RegExp('/tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tontineSample = { codeAssociation: 'Agent Borders COM' };

  let tontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tontines/${tontine.id}`,
      }).then(() => {
        tontine = undefined;
      });
    }
  });

  it('Tontines menu should load Tontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tontine').should('exist');
    cy.url().should('match', tontinePageUrlPattern);
  });

  describe('Tontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tontine/new$'));
        cy.getEntityCreateUpdateHeading('Tontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tontines',
          body: tontineSample,
        }).then(({ body }) => {
          tontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tontines?page=0&size=20>; rel="last",<http://localhost/api/tontines?page=0&size=20>; rel="first"',
              },
              body: [tontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tontinePageUrlPattern);
      });

      it('edit button click should load edit Tontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tontinePageUrlPattern);
      });

      it.skip('edit button click should load edit Tontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tontinePageUrlPattern);
      });

      it('last delete button click should delete instance of Tontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tontinePageUrlPattern);

        tontine = undefined;
      });
    });
  });

  describe('new Tontine page', () => {
    beforeEach(() => {
      cy.visit(`${tontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tontine');
    });

    it('should create an instance of Tontine', () => {
      cy.get(`[data-cy="codeAssociation"]`).type('deposit e-commerce Borders').should('have.value', 'deposit e-commerce Borders');

      cy.get(`[data-cy="libele"]`).type('de').should('have.value', 'de');

      cy.get(`[data-cy="nombreTour"]`).type('17188').should('have.value', '17188');

      cy.get(`[data-cy="nombreMaxPersonne"]`).type('3148').should('have.value', '3148');

      cy.get(`[data-cy="margeBeneficiaire"]`).type('39098').should('have.value', '39098');

      cy.get(`[data-cy="montantPart"]`).type('86558').should('have.value', '86558');

      cy.get(`[data-cy="amandeEchec"]`).type('76316').should('have.value', '76316');

      cy.get(`[data-cy="dateDebut"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="dateFin"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="statutTontine"]`).select('CREE');

      cy.get(`[data-cy="description"]`).type('Handmade').should('have.value', 'Handmade');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tontinePageUrlPattern);
    });
  });
});
