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

describe('Evenement e2e test', () => {
  const evenementPageUrl = '/evenement';
  const evenementPageUrlPattern = new RegExp('/evenement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const evenementSample = {};

  let evenement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/evenements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/evenements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/evenements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (evenement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evenements/${evenement.id}`,
      }).then(() => {
        evenement = undefined;
      });
    }
  });

  it('Evenements menu should load Evenements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('evenement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Evenement').should('exist');
    cy.url().should('match', evenementPageUrlPattern);
  });

  describe('Evenement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(evenementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Evenement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/evenement/new$'));
        cy.getEntityCreateUpdateHeading('Evenement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/evenements',
          body: evenementSample,
        }).then(({ body }) => {
          evenement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/evenements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/evenements?page=0&size=20>; rel="last",<http://localhost/api/evenements?page=0&size=20>; rel="first"',
              },
              body: [evenement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(evenementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Evenement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('evenement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementPageUrlPattern);
      });

      it('edit button click should load edit Evenement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Evenement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementPageUrlPattern);
      });

      it.skip('edit button click should load edit Evenement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Evenement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementPageUrlPattern);
      });

      it('last delete button click should delete instance of Evenement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('evenement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementPageUrlPattern);

        evenement = undefined;
      });
    });
  });

  describe('new Evenement page', () => {
    beforeEach(() => {
      cy.visit(`${evenementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Evenement');
    });

    it('should create an instance of Evenement', () => {
      cy.get(`[data-cy="libele"]`).type('FTP Kids b').should('have.value', 'FTP Kids b');

      cy.get(`[data-cy="codepot"]`).type('Midi-Pyrénées').should('have.value', 'Midi-Pyrénées');

      cy.get(`[data-cy="montantPayer"]`).type('Refined relationships').should('have.value', 'Refined relationships');

      cy.get(`[data-cy="description"]`)
        .type('Mozambique recontextualize Chat-qui-Pêche')
        .should('have.value', 'Mozambique recontextualize Chat-qui-Pêche');

      cy.get(`[data-cy="budget"]`).type('42269').should('have.value', '42269');

      cy.get(`[data-cy="dateEvenement"]`).type('2023-06-08T13:10').blur().should('have.value', '2023-06-08T13:10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        evenement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', evenementPageUrlPattern);
    });
  });
});
