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

describe('Personne e2e test', () => {
  const personnePageUrl = '/personne';
  const personnePageUrlPattern = new RegExp('/personne(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personneSample = {};

  let personne;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/personnes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/personnes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/personnes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personne) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/personnes/${personne.id}`,
      }).then(() => {
        personne = undefined;
      });
    }
  });

  it('Personnes menu should load Personnes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('personne');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Personne').should('exist');
    cy.url().should('match', personnePageUrlPattern);
  });

  describe('Personne page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personnePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Personne page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/personne/new$'));
        cy.getEntityCreateUpdateHeading('Personne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personnePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/personnes',
          body: personneSample,
        }).then(({ body }) => {
          personne = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/personnes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/personnes?page=0&size=20>; rel="last",<http://localhost/api/personnes?page=0&size=20>; rel="first"',
              },
              body: [personne],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(personnePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Personne page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personne');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personnePageUrlPattern);
      });

      it('edit button click should load edit Personne page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Personne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personnePageUrlPattern);
      });

      it.skip('edit button click should load edit Personne page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Personne');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personnePageUrlPattern);
      });

      it('last delete button click should delete instance of Personne', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('personne').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personnePageUrlPattern);

        personne = undefined;
      });
    });
  });

  describe('new Personne page', () => {
    beforeEach(() => {
      cy.visit(`${personnePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Personne');
    });

    it('should create an instance of Personne', () => {
      cy.get(`[data-cy="idUser"]`).type('31616').should('have.value', '31616');

      cy.get(`[data-cy="codeAssociation"]`).type('8127').should('have.value', '8127');

      cy.get(`[data-cy="matricule"]`).type('European').should('have.value', 'European');

      cy.get(`[data-cy="nom"]`).type('Checking').should('have.value', 'Checking');

      cy.get(`[data-cy="prenom"]`).type('Denar').should('have.value', 'Denar');

      cy.get(`[data-cy="dateNaissance"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="lieuNaissance"]`).type('59799').should('have.value', '59799');

      cy.get(`[data-cy="dateInscription"]`).type('2023-06-08T06:32').blur().should('have.value', '2023-06-08T06:32');

      cy.get(`[data-cy="profession"]`).type('Salad').should('have.value', 'Salad');

      cy.get(`[data-cy="sexe"]`).select('FEMININ');

      cy.get(`[data-cy="photoPath"]`).type('Frozen').should('have.value', 'Frozen');

      cy.get(`[data-cy="dateIntegration"]`).type('2023-06-08T00:28').blur().should('have.value', '2023-06-08T00:28');

      cy.get(`[data-cy="isAdmin"]`).should('not.be.checked');
      cy.get(`[data-cy="isAdmin"]`).click().should('be.checked');

      cy.get(`[data-cy="isDonateur"]`).should('not.be.checked');
      cy.get(`[data-cy="isDonateur"]`).click().should('be.checked');

      cy.get(`[data-cy="isBenevole"]`).should('not.be.checked');
      cy.get(`[data-cy="isBenevole"]`).click().should('be.checked');

      cy.get(`[data-cy="typePersonne"]`).select('ADHERENT');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        personne = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', personnePageUrlPattern);
    });
  });
});
