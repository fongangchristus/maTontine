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

describe('Pot e2e test', () => {
  const potPageUrl = '/pot';
  const potPageUrlPattern = new RegExp('/pot(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const potSample = { codepot: 'Small Triple-buffered' };

  let pot;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pots+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pots').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pots/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pot) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pots/${pot.id}`,
      }).then(() => {
        pot = undefined;
      });
    }
  });

  it('Pots menu should load Pots page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pot');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pot').should('exist');
    cy.url().should('match', potPageUrlPattern);
  });

  describe('Pot page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(potPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pot page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pot/new$'));
        cy.getEntityCreateUpdateHeading('Pot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', potPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pots',
          body: potSample,
        }).then(({ body }) => {
          pot = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pots+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pots?page=0&size=20>; rel="last",<http://localhost/api/pots?page=0&size=20>; rel="first"',
              },
              body: [pot],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(potPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pot page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pot');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', potPageUrlPattern);
      });

      it('edit button click should load edit Pot page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', potPageUrlPattern);
      });

      it.skip('edit button click should load edit Pot page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pot');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', potPageUrlPattern);
      });

      it('last delete button click should delete instance of Pot', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', potPageUrlPattern);

        pot = undefined;
      });
    });
  });

  describe('new Pot page', () => {
    beforeEach(() => {
      cy.visit(`${potPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pot');
    });

    it('should create an instance of Pot', () => {
      cy.get(`[data-cy="libele"]`).type('Ball').should('have.value', 'Ball');

      cy.get(`[data-cy="codepot"]`).type('Wooden').should('have.value', 'Wooden');

      cy.get(`[data-cy="montantCible"]`).type('45063').should('have.value', '45063');

      cy.get(`[data-cy="description"]`).type('Directeur transmitting').should('have.value', 'Directeur transmitting');

      cy.get(`[data-cy="dateDebutCollecte"]`).type('2023-06-07').blur().should('have.value', '2023-06-07');

      cy.get(`[data-cy="dateFinCollecte"]`).type('2023-06-07').blur().should('have.value', '2023-06-07');

      cy.get(`[data-cy="statut"]`).select('FERMEE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        pot = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', potPageUrlPattern);
    });
  });
});
