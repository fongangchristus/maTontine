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

describe('TypeEvenement e2e test', () => {
  const typeEvenementPageUrl = '/type-evenement';
  const typeEvenementPageUrlPattern = new RegExp('/type-evenement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeEvenementSample = {};

  let typeEvenement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/type-evenements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/type-evenements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/type-evenements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeEvenement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-evenements/${typeEvenement.id}`,
      }).then(() => {
        typeEvenement = undefined;
      });
    }
  });

  it('TypeEvenements menu should load TypeEvenements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('type-evenement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeEvenement').should('exist');
    cy.url().should('match', typeEvenementPageUrlPattern);
  });

  describe('TypeEvenement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeEvenementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeEvenement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/type-evenement/new$'));
        cy.getEntityCreateUpdateHeading('TypeEvenement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeEvenementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/type-evenements',
          body: typeEvenementSample,
        }).then(({ body }) => {
          typeEvenement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/type-evenements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/type-evenements?page=0&size=20>; rel="last",<http://localhost/api/type-evenements?page=0&size=20>; rel="first"',
              },
              body: [typeEvenement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeEvenementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeEvenement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeEvenement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeEvenementPageUrlPattern);
      });

      it('edit button click should load edit TypeEvenement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeEvenement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeEvenementPageUrlPattern);
      });

      it.skip('edit button click should load edit TypeEvenement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeEvenement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeEvenementPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeEvenement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeEvenement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeEvenementPageUrlPattern);

        typeEvenement = undefined;
      });
    });
  });

  describe('new TypeEvenement page', () => {
    beforeEach(() => {
      cy.visit(`${typeEvenementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeEvenement');
    });

    it('should create an instance of TypeEvenement', () => {
      cy.get(`[data-cy="libele"]`).type('CFP').should('have.value', 'CFP');

      cy.get(`[data-cy="observation"]`).type('Account archive').should('have.value', 'Account archive');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeEvenement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeEvenementPageUrlPattern);
    });
  });
});
