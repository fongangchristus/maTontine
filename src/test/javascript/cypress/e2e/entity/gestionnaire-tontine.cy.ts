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

describe('GestionnaireTontine e2e test', () => {
  const gestionnaireTontinePageUrl = '/gestionnaire-tontine';
  const gestionnaireTontinePageUrlPattern = new RegExp('/gestionnaire-tontine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const gestionnaireTontineSample = {};

  let gestionnaireTontine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gestionnaire-tontines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gestionnaire-tontines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gestionnaire-tontines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (gestionnaireTontine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gestionnaire-tontines/${gestionnaireTontine.id}`,
      }).then(() => {
        gestionnaireTontine = undefined;
      });
    }
  });

  it('GestionnaireTontines menu should load GestionnaireTontines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gestionnaire-tontine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GestionnaireTontine').should('exist');
    cy.url().should('match', gestionnaireTontinePageUrlPattern);
  });

  describe('GestionnaireTontine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(gestionnaireTontinePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GestionnaireTontine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/gestionnaire-tontine/new$'));
        cy.getEntityCreateUpdateHeading('GestionnaireTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireTontinePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gestionnaire-tontines',
          body: gestionnaireTontineSample,
        }).then(({ body }) => {
          gestionnaireTontine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gestionnaire-tontines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/gestionnaire-tontines?page=0&size=20>; rel="last",<http://localhost/api/gestionnaire-tontines?page=0&size=20>; rel="first"',
              },
              body: [gestionnaireTontine],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(gestionnaireTontinePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GestionnaireTontine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('gestionnaireTontine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireTontinePageUrlPattern);
      });

      it('edit button click should load edit GestionnaireTontine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GestionnaireTontine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireTontinePageUrlPattern);
      });

      it.skip('edit button click should load edit GestionnaireTontine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GestionnaireTontine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireTontinePageUrlPattern);
      });

      it('last delete button click should delete instance of GestionnaireTontine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('gestionnaireTontine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireTontinePageUrlPattern);

        gestionnaireTontine = undefined;
      });
    });
  });

  describe('new GestionnaireTontine page', () => {
    beforeEach(() => {
      cy.visit(`${gestionnaireTontinePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GestionnaireTontine');
    });

    it('should create an instance of GestionnaireTontine', () => {
      cy.get(`[data-cy="matriculeAdherent"]`).type('Superviseur Granite Soft').should('have.value', 'Superviseur Granite Soft');

      cy.get(`[data-cy="codeTontine"]`).type('bi-directional').should('have.value', 'bi-directional');

      cy.get(`[data-cy="datePriseFonction"]`).type('2023-06-07').blur().should('have.value', '2023-06-07');

      cy.get(`[data-cy="dateFinFonction"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        gestionnaireTontine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', gestionnaireTontinePageUrlPattern);
    });
  });
});
