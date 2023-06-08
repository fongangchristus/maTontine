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

describe('Sanction e2e test', () => {
  const sanctionPageUrl = '/sanction';
  const sanctionPageUrlPattern = new RegExp('/sanction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sanctionSample = { matriculeAdherent: 'Bulgarian optimize' };

  let sanction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sanctions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sanctions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sanctions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sanction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sanctions/${sanction.id}`,
      }).then(() => {
        sanction = undefined;
      });
    }
  });

  it('Sanctions menu should load Sanctions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sanction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Sanction').should('exist');
    cy.url().should('match', sanctionPageUrlPattern);
  });

  describe('Sanction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sanctionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Sanction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sanction/new$'));
        cy.getEntityCreateUpdateHeading('Sanction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sanctions',
          body: sanctionSample,
        }).then(({ body }) => {
          sanction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sanctions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sanctions?page=0&size=20>; rel="last",<http://localhost/api/sanctions?page=0&size=20>; rel="first"',
              },
              body: [sanction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sanctionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Sanction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sanction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionPageUrlPattern);
      });

      it('edit button click should load edit Sanction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Sanction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionPageUrlPattern);
      });

      it.skip('edit button click should load edit Sanction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Sanction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionPageUrlPattern);
      });

      it('last delete button click should delete instance of Sanction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sanction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionPageUrlPattern);

        sanction = undefined;
      });
    });
  });

  describe('new Sanction page', () => {
    beforeEach(() => {
      cy.visit(`${sanctionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Sanction');
    });

    it('should create an instance of Sanction', () => {
      cy.get(`[data-cy="libelle"]`).type('state').should('have.value', 'state');

      cy.get(`[data-cy="matriculeAdherent"]`).type('open-source Right-sized b').should('have.value', 'open-source Right-sized b');

      cy.get(`[data-cy="dateSanction"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="motifSanction"]`).type('TCP optical a').should('have.value', 'TCP optical a');

      cy.get(`[data-cy="description"]`).type('d&#39;Orsel').should('have.value', 'd&#39;Orsel');

      cy.get(`[data-cy="codeActivite"]`).type('Birmanie').should('have.value', 'Birmanie');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sanction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sanctionPageUrlPattern);
    });
  });
});
