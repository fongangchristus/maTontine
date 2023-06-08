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

describe('ContributionPot e2e test', () => {
  const contributionPotPageUrl = '/contribution-pot';
  const contributionPotPageUrlPattern = new RegExp('/contribution-pot(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contributionPotSample = { matriculeContributeur: 'generation Fundamental' };

  let contributionPot;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contribution-pots+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contribution-pots').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contribution-pots/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contributionPot) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contribution-pots/${contributionPot.id}`,
      }).then(() => {
        contributionPot = undefined;
      });
    }
  });

  it('ContributionPots menu should load ContributionPots page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contribution-pot');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContributionPot').should('exist');
    cy.url().should('match', contributionPotPageUrlPattern);
  });

  describe('ContributionPot page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contributionPotPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContributionPot page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/contribution-pot/new$'));
        cy.getEntityCreateUpdateHeading('ContributionPot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contributionPotPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contribution-pots',
          body: contributionPotSample,
        }).then(({ body }) => {
          contributionPot = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contribution-pots+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contribution-pots?page=0&size=20>; rel="last",<http://localhost/api/contribution-pots?page=0&size=20>; rel="first"',
              },
              body: [contributionPot],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contributionPotPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContributionPot page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contributionPot');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contributionPotPageUrlPattern);
      });

      it('edit button click should load edit ContributionPot page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContributionPot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contributionPotPageUrlPattern);
      });

      it.skip('edit button click should load edit ContributionPot page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContributionPot');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contributionPotPageUrlPattern);
      });

      it('last delete button click should delete instance of ContributionPot', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contributionPot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contributionPotPageUrlPattern);

        contributionPot = undefined;
      });
    });
  });

  describe('new ContributionPot page', () => {
    beforeEach(() => {
      cy.visit(`${contributionPotPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContributionPot');
    });

    it('should create an instance of ContributionPot', () => {
      cy.get(`[data-cy="identifiant"]`).type('parsing').should('have.value', 'parsing');

      cy.get(`[data-cy="matriculeContributeur"]`).type('Metal').should('have.value', 'Metal');

      cy.get(`[data-cy="montantContribution"]`).type('8746').should('have.value', '8746');

      cy.get(`[data-cy="dateContribution"]`).type('2023-06-08T06:05').blur().should('have.value', '2023-06-08T06:05');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        contributionPot = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', contributionPotPageUrlPattern);
    });
  });
});
