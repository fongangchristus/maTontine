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

describe('Banque e2e test', () => {
  const banquePageUrl = '/banque';
  const banquePageUrlPattern = new RegExp('/banque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const banqueSample = { codeAssociation: 'programming e-commerce engineer' };

  let banque;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/banques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/banques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/banques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (banque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/banques/${banque.id}`,
      }).then(() => {
        banque = undefined;
      });
    }
  });

  it('Banques menu should load Banques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('banque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Banque').should('exist');
    cy.url().should('match', banquePageUrlPattern);
  });

  describe('Banque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(banquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Banque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/banque/new$'));
        cy.getEntityCreateUpdateHeading('Banque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', banquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/banques',
          body: banqueSample,
        }).then(({ body }) => {
          banque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/banques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/banques?page=0&size=20>; rel="last",<http://localhost/api/banques?page=0&size=20>; rel="first"',
              },
              body: [banque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(banquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Banque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('banque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', banquePageUrlPattern);
      });

      it('edit button click should load edit Banque page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Banque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', banquePageUrlPattern);
      });

      it.skip('edit button click should load edit Banque page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Banque');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', banquePageUrlPattern);
      });

      it('last delete button click should delete instance of Banque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('banque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', banquePageUrlPattern);

        banque = undefined;
      });
    });
  });

  describe('new Banque page', () => {
    beforeEach(() => {
      cy.visit(`${banquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Banque');
    });

    it('should create an instance of Banque', () => {
      cy.get(`[data-cy="codeAssociation"]`).type('Rubber magnetic whiteboard').should('have.value', 'Rubber magnetic whiteboard');

      cy.get(`[data-cy="libelle"]`).type('quantify b').should('have.value', 'quantify b');

      cy.get(`[data-cy="description"]`).type('generate reboot copy').should('have.value', 'generate reboot copy');

      cy.get(`[data-cy="dateOuverture"]`).type('2023-06-07T23:11').blur().should('have.value', '2023-06-07T23:11');

      cy.get(`[data-cy="dateCloture"]`).type('2023-06-08T06:20').blur().should('have.value', '2023-06-08T06:20');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        banque = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', banquePageUrlPattern);
    });
  });
});
