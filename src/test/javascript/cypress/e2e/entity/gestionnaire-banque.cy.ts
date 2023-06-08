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

describe('GestionnaireBanque e2e test', () => {
  const gestionnaireBanquePageUrl = '/gestionnaire-banque';
  const gestionnaireBanquePageUrlPattern = new RegExp('/gestionnaire-banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const gestionnaireBanqueSample = {};

  let gestionnaireBanque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gestionnaire-banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gestionnaire-banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gestionnaire-banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (gestionnaireBanque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gestionnaire-banques/${gestionnaireBanque.id}`,
      }).then(() => {
        gestionnaireBanque = undefined;
      });
    }
  });

  it('GestionnaireBanques menu should load GestionnaireBanques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gestionnaire-banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GestionnaireBanque').should('exist');
    cy.url().should('match', gestionnaireBanquePageUrlPattern);
  });

  describe('GestionnaireBanque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(gestionnaireBanquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GestionnaireBanque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/gestionnaire-banque/new$'));
        cy.getEntityCreateUpdateHeading('GestionnaireBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireBanquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gestionnaire-banques',
          body: gestionnaireBanqueSample,
        }).then(({ body }) => {
          gestionnaireBanque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gestionnaire-banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/gestionnaire-banques?page=0&size=20>; rel="last",<http://localhost/api/gestionnaire-banques?page=0&size=20>; rel="first"',
              },
              body: [gestionnaireBanque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(gestionnaireBanquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GestionnaireBanque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('gestionnaireBanque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireBanquePageUrlPattern);
      });

      it('edit button click should load edit GestionnaireBanque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GestionnaireBanque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireBanquePageUrlPattern);
      });

      it.skip('edit button click should load edit GestionnaireBanque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GestionnaireBanque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireBanquePageUrlPattern);
      });

      it('last delete button click should delete instance of GestionnaireBanque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('gestionnaireBanque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', gestionnaireBanquePageUrlPattern);

        gestionnaireBanque = undefined;
      });
    });
  });

  describe('new GestionnaireBanque page', () => {
    beforeEach(() => {
      cy.visit(`${gestionnaireBanquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GestionnaireBanque');
    });

    it('should create an instance of GestionnaireBanque', () => {
      cy.get(`[data-cy="matriculeMembre"]`).type('b Beauty').should('have.value', 'b Beauty');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        gestionnaireBanque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', gestionnaireBanquePageUrlPattern);
    });
  });
});
