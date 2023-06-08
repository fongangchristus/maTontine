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

describe('TypePot e2e test', () => {
  const typePotPageUrl = '/type-pot';
  const typePotPageUrlPattern = new RegExp('/type-pot(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typePotSample = { libele: 'hacking' };

  let typePot;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/type-pots+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/type-pots').as('postEntityRequest');
    cy.intercept('DELETE', '/api/type-pots/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typePot) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-pots/${typePot.id}`,
      }).then(() => {
        typePot = undefined;
      });
    }
  });

  it('TypePots menu should load TypePots page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('type-pot');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypePot').should('exist');
    cy.url().should('match', typePotPageUrlPattern);
  });

  describe('TypePot page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typePotPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypePot page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/type-pot/new$'));
        cy.getEntityCreateUpdateHeading('TypePot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typePotPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/type-pots',
          body: typePotSample,
        }).then(({ body }) => {
          typePot = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/type-pots+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/type-pots?page=0&size=20>; rel="last",<http://localhost/api/type-pots?page=0&size=20>; rel="first"',
              },
              body: [typePot],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(typePotPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypePot page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typePot');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typePotPageUrlPattern);
      });

      it('edit button click should load edit TypePot page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypePot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typePotPageUrlPattern);
      });

      it.skip('edit button click should load edit TypePot page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypePot');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typePotPageUrlPattern);
      });

      it('last delete button click should delete instance of TypePot', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typePot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typePotPageUrlPattern);

        typePot = undefined;
      });
    });
  });

  describe('new TypePot page', () => {
    beforeEach(() => {
      cy.visit(`${typePotPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypePot');
    });

    it('should create an instance of TypePot', () => {
      cy.get(`[data-cy="libele"]`).type('composite').should('have.value', 'composite');

      cy.get(`[data-cy="descrption"]`).type('parse invoice systems').should('have.value', 'parse invoice systems');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typePot = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typePotPageUrlPattern);
    });
  });
});
