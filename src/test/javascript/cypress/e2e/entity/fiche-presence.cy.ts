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

describe('FichePresence e2e test', () => {
  const fichePresencePageUrl = '/fiche-presence';
  const fichePresencePageUrlPattern = new RegExp('/fiche-presence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fichePresenceSample = {};

  let fichePresence;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fiche-presences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fiche-presences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fiche-presences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fichePresence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiche-presences/${fichePresence.id}`,
      }).then(() => {
        fichePresence = undefined;
      });
    }
  });

  it('FichePresences menu should load FichePresences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fiche-presence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FichePresence').should('exist');
    cy.url().should('match', fichePresencePageUrlPattern);
  });

  describe('FichePresence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fichePresencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FichePresence page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fiche-presence/new$'));
        cy.getEntityCreateUpdateHeading('FichePresence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fichePresencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fiche-presences',
          body: fichePresenceSample,
        }).then(({ body }) => {
          fichePresence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fiche-presences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fiche-presences?page=0&size=20>; rel="last",<http://localhost/api/fiche-presences?page=0&size=20>; rel="first"',
              },
              body: [fichePresence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fichePresencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FichePresence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fichePresence');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fichePresencePageUrlPattern);
      });

      it('edit button click should load edit FichePresence page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FichePresence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fichePresencePageUrlPattern);
      });

      it.skip('edit button click should load edit FichePresence page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FichePresence');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fichePresencePageUrlPattern);
      });

      it('last delete button click should delete instance of FichePresence', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fichePresence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fichePresencePageUrlPattern);

        fichePresence = undefined;
      });
    });
  });

  describe('new FichePresence page', () => {
    beforeEach(() => {
      cy.visit(`${fichePresencePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FichePresence');
    });

    it('should create an instance of FichePresence', () => {
      cy.get(`[data-cy="libelle"]`).type('Haute-Normandie orchid a').should('have.value', 'Haute-Normandie orchid a');

      cy.get(`[data-cy="dateJour"]`).type('2023-06-07T20:03').blur().should('have.value', '2023-06-07T20:03');

      cy.get(`[data-cy="description"]`).type('Metal b SDD').should('have.value', 'Metal b SDD');

      cy.get(`[data-cy="codeAssemble"]`).type('Shoes').should('have.value', 'Shoes');

      cy.get(`[data-cy="codeEvenement"]`).type('systemic navigating').should('have.value', 'systemic navigating');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fichePresence = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fichePresencePageUrlPattern);
    });
  });
});
