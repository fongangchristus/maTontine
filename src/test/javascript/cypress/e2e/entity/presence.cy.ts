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

describe('Presence e2e test', () => {
  const presencePageUrl = '/presence';
  const presencePageUrlPattern = new RegExp('/presence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const presenceSample = { matriculeAdherant: 'Ergonomic Soudan' };

  let presence;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/presences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/presences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/presences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (presence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/presences/${presence.id}`,
      }).then(() => {
        presence = undefined;
      });
    }
  });

  it('Presences menu should load Presences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('presence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Presence').should('exist');
    cy.url().should('match', presencePageUrlPattern);
  });

  describe('Presence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(presencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Presence page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/presence/new$'));
        cy.getEntityCreateUpdateHeading('Presence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', presencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/presences',
          body: presenceSample,
        }).then(({ body }) => {
          presence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/presences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/presences?page=0&size=20>; rel="last",<http://localhost/api/presences?page=0&size=20>; rel="first"',
              },
              body: [presence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(presencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Presence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('presence');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', presencePageUrlPattern);
      });

      it('edit button click should load edit Presence page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Presence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', presencePageUrlPattern);
      });

      it.skip('edit button click should load edit Presence page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Presence');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', presencePageUrlPattern);
      });

      it('last delete button click should delete instance of Presence', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('presence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', presencePageUrlPattern);

        presence = undefined;
      });
    });
  });

  describe('new Presence page', () => {
    beforeEach(() => {
      cy.visit(`${presencePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Presence');
    });

    it('should create an instance of Presence', () => {
      cy.get(`[data-cy="matriculeAdherant"]`).type('Grocery copying').should('have.value', 'Grocery copying');

      cy.get(`[data-cy="statutPresence"]`).select('RETARD');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        presence = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', presencePageUrlPattern);
    });
  });
});
