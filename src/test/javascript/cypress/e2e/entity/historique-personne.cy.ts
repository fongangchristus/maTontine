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

describe('HistoriquePersonne e2e test', () => {
  const historiquePersonnePageUrl = '/historique-personne';
  const historiquePersonnePageUrlPattern = new RegExp('/historique-personne(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const historiquePersonneSample = { matriculePersonne: 'JSON c Malaisie' };

  let historiquePersonne;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/historique-personnes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historique-personnes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historique-personnes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (historiquePersonne) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historique-personnes/${historiquePersonne.id}`,
      }).then(() => {
        historiquePersonne = undefined;
      });
    }
  });

  it('HistoriquePersonnes menu should load HistoriquePersonnes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historique-personne');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HistoriquePersonne').should('exist');
    cy.url().should('match', historiquePersonnePageUrlPattern);
  });

  describe('HistoriquePersonne page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historiquePersonnePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HistoriquePersonne page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/historique-personne/new$'));
        cy.getEntityCreateUpdateHeading('HistoriquePersonne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiquePersonnePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/historique-personnes',
          body: historiquePersonneSample,
        }).then(({ body }) => {
          historiquePersonne = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/historique-personnes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/historique-personnes?page=0&size=20>; rel="last",<http://localhost/api/historique-personnes?page=0&size=20>; rel="first"',
              },
              body: [historiquePersonne],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(historiquePersonnePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HistoriquePersonne page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historiquePersonne');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiquePersonnePageUrlPattern);
      });

      it('edit button click should load edit HistoriquePersonne page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriquePersonne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiquePersonnePageUrlPattern);
      });

      it.skip('edit button click should load edit HistoriquePersonne page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriquePersonne');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiquePersonnePageUrlPattern);
      });

      it('last delete button click should delete instance of HistoriquePersonne', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('historiquePersonne').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiquePersonnePageUrlPattern);

        historiquePersonne = undefined;
      });
    });
  });

  describe('new HistoriquePersonne page', () => {
    beforeEach(() => {
      cy.visit(`${historiquePersonnePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HistoriquePersonne');
    });

    it('should create an instance of HistoriquePersonne', () => {
      cy.get(`[data-cy="dateAction"]`).type('2023-06-08T12:24').blur().should('have.value', '2023-06-08T12:24');

      cy.get(`[data-cy="matriculePersonne"]`).type('navigating Enterprise-wide').should('have.value', 'navigating Enterprise-wide');

      cy.get(`[data-cy="action"]`).type('Investment').should('have.value', 'Investment');

      cy.get(`[data-cy="result"]`).type('Picardie Liberian').should('have.value', 'Picardie Liberian');

      cy.get(`[data-cy="description"]`).type('des').should('have.value', 'des');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        historiquePersonne = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', historiquePersonnePageUrlPattern);
    });
  });
});
