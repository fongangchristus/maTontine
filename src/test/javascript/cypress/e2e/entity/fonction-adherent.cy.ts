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

describe('FonctionAdherent e2e test', () => {
  const fonctionAdherentPageUrl = '/fonction-adherent';
  const fonctionAdherentPageUrlPattern = new RegExp('/fonction-adherent(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fonctionAdherentSample = { matriculeAdherent: 'user-centric', codeFonction: 'a', datePriseFonction: '2023-06-08', actif: true };

  let fonctionAdherent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fonction-adherents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fonction-adherents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fonction-adherents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fonctionAdherent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fonction-adherents/${fonctionAdherent.id}`,
      }).then(() => {
        fonctionAdherent = undefined;
      });
    }
  });

  it('FonctionAdherents menu should load FonctionAdherents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fonction-adherent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FonctionAdherent').should('exist');
    cy.url().should('match', fonctionAdherentPageUrlPattern);
  });

  describe('FonctionAdherent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fonctionAdherentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FonctionAdherent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fonction-adherent/new$'));
        cy.getEntityCreateUpdateHeading('FonctionAdherent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionAdherentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fonction-adherents',
          body: fonctionAdherentSample,
        }).then(({ body }) => {
          fonctionAdherent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fonction-adherents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fonction-adherents?page=0&size=20>; rel="last",<http://localhost/api/fonction-adherents?page=0&size=20>; rel="first"',
              },
              body: [fonctionAdherent],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fonctionAdherentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FonctionAdherent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fonctionAdherent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionAdherentPageUrlPattern);
      });

      it('edit button click should load edit FonctionAdherent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FonctionAdherent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionAdherentPageUrlPattern);
      });

      it.skip('edit button click should load edit FonctionAdherent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FonctionAdherent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionAdherentPageUrlPattern);
      });

      it('last delete button click should delete instance of FonctionAdherent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fonctionAdherent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fonctionAdherentPageUrlPattern);

        fonctionAdherent = undefined;
      });
    });
  });

  describe('new FonctionAdherent page', () => {
    beforeEach(() => {
      cy.visit(`${fonctionAdherentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FonctionAdherent');
    });

    it('should create an instance of FonctionAdherent', () => {
      cy.get(`[data-cy="matriculeAdherent"]`).type('transform Loire Decentralized').should('have.value', 'transform Loire Decentralized');

      cy.get(`[data-cy="codeFonction"]`).type('calculating').should('have.value', 'calculating');

      cy.get(`[data-cy="datePriseFonction"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="dateFinFonction"]`).type('2023-06-07').blur().should('have.value', '2023-06-07');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fonctionAdherent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fonctionAdherentPageUrlPattern);
    });
  });
});
