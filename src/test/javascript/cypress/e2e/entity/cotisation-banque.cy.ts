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

describe('CotisationBanque e2e test', () => {
  const cotisationBanquePageUrl = '/cotisation-banque';
  const cotisationBanquePageUrlPattern = new RegExp('/cotisation-banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cotisationBanqueSample = {};

  let cotisationBanque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cotisation-banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cotisation-banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cotisation-banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cotisationBanque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cotisation-banques/${cotisationBanque.id}`,
      }).then(() => {
        cotisationBanque = undefined;
      });
    }
  });

  it('CotisationBanques menu should load CotisationBanques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cotisation-banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CotisationBanque').should('exist');
    cy.url().should('match', cotisationBanquePageUrlPattern);
  });

  describe('CotisationBanque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cotisationBanquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CotisationBanque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cotisation-banque/new$'));
        cy.getEntityCreateUpdateHeading('CotisationBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationBanquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cotisation-banques',
          body: cotisationBanqueSample,
        }).then(({ body }) => {
          cotisationBanque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cotisation-banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cotisation-banques?page=0&size=20>; rel="last",<http://localhost/api/cotisation-banques?page=0&size=20>; rel="first"',
              },
              body: [cotisationBanque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cotisationBanquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CotisationBanque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cotisationBanque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationBanquePageUrlPattern);
      });

      it('edit button click should load edit CotisationBanque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CotisationBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationBanquePageUrlPattern);
      });

      it.skip('edit button click should load edit CotisationBanque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CotisationBanque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationBanquePageUrlPattern);
      });

      it('last delete button click should delete instance of CotisationBanque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cotisationBanque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cotisationBanquePageUrlPattern);

        cotisationBanque = undefined;
      });
    });
  });

  describe('new CotisationBanque page', () => {
    beforeEach(() => {
      cy.visit(`${cotisationBanquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CotisationBanque');
    });

    it('should create an instance of CotisationBanque', () => {
      cy.get(`[data-cy="libelle"]`).type('initiatives Ergonomic de').should('have.value', 'initiatives Ergonomic de');

      cy.get(`[data-cy="montant"]`).type('46642').should('have.value', '46642');

      cy.get(`[data-cy="dateCotisation"]`).type('2023-06-07T15:17').blur().should('have.value', '2023-06-07T15:17');

      cy.get(`[data-cy="montantCotise"]`).type('82338').should('have.value', '82338');

      cy.get(`[data-cy="commentaire"]`).type('up').should('have.value', 'up');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cotisationBanque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cotisationBanquePageUrlPattern);
    });
  });
});
