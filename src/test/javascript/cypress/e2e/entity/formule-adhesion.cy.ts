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

describe('FormuleAdhesion e2e test', () => {
  const formuleAdhesionPageUrl = '/formule-adhesion';
  const formuleAdhesionPageUrlPattern = new RegExp('/formule-adhesion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formuleAdhesionSample = {};

  let formuleAdhesion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/formule-adhesions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/formule-adhesions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/formule-adhesions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formuleAdhesion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/formule-adhesions/${formuleAdhesion.id}`,
      }).then(() => {
        formuleAdhesion = undefined;
      });
    }
  });

  it('FormuleAdhesions menu should load FormuleAdhesions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('formule-adhesion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormuleAdhesion').should('exist');
    cy.url().should('match', formuleAdhesionPageUrlPattern);
  });

  describe('FormuleAdhesion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formuleAdhesionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormuleAdhesion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/formule-adhesion/new$'));
        cy.getEntityCreateUpdateHeading('FormuleAdhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formuleAdhesionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/formule-adhesions',
          body: formuleAdhesionSample,
        }).then(({ body }) => {
          formuleAdhesion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/formule-adhesions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/formule-adhesions?page=0&size=20>; rel="last",<http://localhost/api/formule-adhesions?page=0&size=20>; rel="first"',
              },
              body: [formuleAdhesion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(formuleAdhesionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormuleAdhesion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formuleAdhesion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formuleAdhesionPageUrlPattern);
      });

      it('edit button click should load edit FormuleAdhesion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormuleAdhesion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formuleAdhesionPageUrlPattern);
      });

      it.skip('edit button click should load edit FormuleAdhesion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormuleAdhesion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formuleAdhesionPageUrlPattern);
      });

      it('last delete button click should delete instance of FormuleAdhesion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formuleAdhesion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formuleAdhesionPageUrlPattern);

        formuleAdhesion = undefined;
      });
    });
  });

  describe('new FormuleAdhesion page', () => {
    beforeEach(() => {
      cy.visit(`${formuleAdhesionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormuleAdhesion');
    });

    it('should create an instance of FormuleAdhesion', () => {
      cy.get(`[data-cy="adhesionPeriodique"]`).should('not.be.checked');
      cy.get(`[data-cy="adhesionPeriodique"]`).click().should('be.checked');

      cy.get(`[data-cy="dateDebut"]`).type('2023-06-07T19:51').blur().should('have.value', '2023-06-07T19:51');

      cy.get(`[data-cy="dureeAdhesionMois"]`).type('60519').should('have.value', '60519');

      cy.get(`[data-cy="montantLibre"]`).should('not.be.checked');
      cy.get(`[data-cy="montantLibre"]`).click().should('be.checked');

      cy.get(`[data-cy="description"]`).type('Auvergne Berkshire').should('have.value', 'Auvergne Berkshire');

      cy.get(`[data-cy="tarif"]`).type('35219').should('have.value', '35219');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formuleAdhesion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formuleAdhesionPageUrlPattern);
    });
  });
});
