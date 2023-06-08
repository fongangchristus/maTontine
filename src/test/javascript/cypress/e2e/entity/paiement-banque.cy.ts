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

describe('PaiementBanque e2e test', () => {
  const paiementBanquePageUrl = '/paiement-banque';
  const paiementBanquePageUrlPattern = new RegExp('/paiement-banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementBanqueSample = { referencePaiement: 'engage Buckinghamshire Bonaparte' };

  let paiementBanque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paiement-banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paiement-banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paiement-banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementBanque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paiement-banques/${paiementBanque.id}`,
      }).then(() => {
        paiementBanque = undefined;
      });
    }
  });

  it('PaiementBanques menu should load PaiementBanques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paiement-banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementBanque').should('exist');
    cy.url().should('match', paiementBanquePageUrlPattern);
  });

  describe('PaiementBanque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementBanquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementBanque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paiement-banque/new$'));
        cy.getEntityCreateUpdateHeading('PaiementBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementBanquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paiement-banques',
          body: paiementBanqueSample,
        }).then(({ body }) => {
          paiementBanque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paiement-banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/paiement-banques?page=0&size=20>; rel="last",<http://localhost/api/paiement-banques?page=0&size=20>; rel="first"',
              },
              body: [paiementBanque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementBanquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementBanque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementBanque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementBanquePageUrlPattern);
      });

      it('edit button click should load edit PaiementBanque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementBanquePageUrlPattern);
      });

      it.skip('edit button click should load edit PaiementBanque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementBanque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementBanquePageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementBanque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementBanque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementBanquePageUrlPattern);

        paiementBanque = undefined;
      });
    });
  });

  describe('new PaiementBanque page', () => {
    beforeEach(() => {
      cy.visit(`${paiementBanquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementBanque');
    });

    it('should create an instance of PaiementBanque', () => {
      cy.get(`[data-cy="referencePaiement"]`).type('orchestrate').should('have.value', 'orchestrate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementBanque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementBanquePageUrlPattern);
    });
  });
});
