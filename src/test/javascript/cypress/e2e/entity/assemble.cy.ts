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

describe('Assemble e2e test', () => {
  const assemblePageUrl = '/assemble';
  const assemblePageUrlPattern = new RegExp('/assemble(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assembleSample = { codeAssociation: 'Nord-Pas-de-Calais Money global' };

  let assemble;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assembles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assembles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assembles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assemble) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assembles/${assemble.id}`,
      }).then(() => {
        assemble = undefined;
      });
    }
  });

  it('Assembles menu should load Assembles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('assemble');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Assemble').should('exist');
    cy.url().should('match', assemblePageUrlPattern);
  });

  describe('Assemble page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assemblePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Assemble page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/assemble/new$'));
        cy.getEntityCreateUpdateHeading('Assemble');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assemblePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assembles',
          body: assembleSample,
        }).then(({ body }) => {
          assemble = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assembles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/assembles?page=0&size=20>; rel="last",<http://localhost/api/assembles?page=0&size=20>; rel="first"',
              },
              body: [assemble],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assemblePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Assemble page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assemble');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assemblePageUrlPattern);
      });

      it('edit button click should load edit Assemble page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Assemble');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assemblePageUrlPattern);
      });

      it.skip('edit button click should load edit Assemble page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Assemble');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assemblePageUrlPattern);
      });

      it('last delete button click should delete instance of Assemble', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assemble').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assemblePageUrlPattern);

        assemble = undefined;
      });
    });
  });

  describe('new Assemble page', () => {
    beforeEach(() => {
      cy.visit(`${assemblePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Assemble');
    });

    it('should create an instance of Assemble', () => {
      cy.get(`[data-cy="codeAssociation"]`).type('monitor Belarussian transform').should('have.value', 'monitor Belarussian transform');

      cy.get(`[data-cy="libele"]`).type('a').should('have.value', 'a');

      cy.get(`[data-cy="enLigne"]`).should('not.be.checked');
      cy.get(`[data-cy="enLigne"]`).click().should('be.checked');

      cy.get(`[data-cy="dateSeance"]`).type('ROI withdrawal platforms').should('have.value', 'ROI withdrawal platforms');

      cy.get(`[data-cy="lieuSeance"]`).type('deposit Shoes').should('have.value', 'deposit Shoes');

      cy.get(`[data-cy="matriculeMembreRecoit"]`).type('calculating').should('have.value', 'calculating');

      cy.get(`[data-cy="nature"]`).select('MIXTE');

      cy.get(`[data-cy="compteRendu"]`).type('drive').should('have.value', 'drive');

      cy.get(`[data-cy="resumeAssemble"]`).type('Market b').should('have.value', 'Market b');

      cy.get(`[data-cy="documentCRPath"]`).type('circuit Account Synergistic').should('have.value', 'circuit Account Synergistic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assemble = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assemblePageUrlPattern);
    });
  });
});
