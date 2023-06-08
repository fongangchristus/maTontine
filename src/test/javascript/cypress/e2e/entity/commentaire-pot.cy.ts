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

describe('CommentairePot e2e test', () => {
  const commentairePotPageUrl = '/commentaire-pot';
  const commentairePotPageUrlPattern = new RegExp('/commentaire-pot(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const commentairePotSample = { matriculeContributeur: 'Ingenieur a Beauty', identifiantPot: 'Industrial transition Sleek' };

  let commentairePot;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/commentaire-pots+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/commentaire-pots').as('postEntityRequest');
    cy.intercept('DELETE', '/api/commentaire-pots/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (commentairePot) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/commentaire-pots/${commentairePot.id}`,
      }).then(() => {
        commentairePot = undefined;
      });
    }
  });

  it('CommentairePots menu should load CommentairePots page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('commentaire-pot');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommentairePot').should('exist');
    cy.url().should('match', commentairePotPageUrlPattern);
  });

  describe('CommentairePot page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(commentairePotPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommentairePot page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/commentaire-pot/new$'));
        cy.getEntityCreateUpdateHeading('CommentairePot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', commentairePotPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/commentaire-pots',
          body: commentairePotSample,
        }).then(({ body }) => {
          commentairePot = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/commentaire-pots+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/commentaire-pots?page=0&size=20>; rel="last",<http://localhost/api/commentaire-pots?page=0&size=20>; rel="first"',
              },
              body: [commentairePot],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(commentairePotPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommentairePot page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('commentairePot');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', commentairePotPageUrlPattern);
      });

      it('edit button click should load edit CommentairePot page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommentairePot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', commentairePotPageUrlPattern);
      });

      it.skip('edit button click should load edit CommentairePot page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommentairePot');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', commentairePotPageUrlPattern);
      });

      it('last delete button click should delete instance of CommentairePot', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('commentairePot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', commentairePotPageUrlPattern);

        commentairePot = undefined;
      });
    });
  });

  describe('new CommentairePot page', () => {
    beforeEach(() => {
      cy.visit(`${commentairePotPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommentairePot');
    });

    it('should create an instance of CommentairePot', () => {
      cy.get(`[data-cy="matriculeContributeur"]`).type('Shoes').should('have.value', 'Shoes');

      cy.get(`[data-cy="identifiantPot"]`).type('Outdoors').should('have.value', 'Outdoors');

      cy.get(`[data-cy="contenu"]`).type('Balanced').should('have.value', 'Balanced');

      cy.get(`[data-cy="dateComentaire"]`).type('2023-06-08T06:36').blur().should('have.value', '2023-06-08T06:36');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        commentairePot = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', commentairePotPageUrlPattern);
    });
  });
});
