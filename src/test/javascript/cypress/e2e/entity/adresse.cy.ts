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

describe('Adresse e2e test', () => {
  const adressePageUrl = '/adresse';
  const adressePageUrlPattern = new RegExp('/adresse(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adresseSample = { departmentName: 'Poitou-Charentes Concrete Corse' };

  let adresse;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/adresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/adresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/adresses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (adresse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/adresses/${adresse.id}`,
      }).then(() => {
        adresse = undefined;
      });
    }
  });

  it('Adresses menu should load Adresses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('adresse');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Adresse').should('exist');
    cy.url().should('match', adressePageUrlPattern);
  });

  describe('Adresse page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adressePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Adresse page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/adresse/new$'));
        cy.getEntityCreateUpdateHeading('Adresse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adressePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/adresses',
          body: adresseSample,
        }).then(({ body }) => {
          adresse = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/adresses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/adresses?page=0&size=20>; rel="last",<http://localhost/api/adresses?page=0&size=20>; rel="first"',
              },
              body: [adresse],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(adressePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Adresse page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adresse');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adressePageUrlPattern);
      });

      it('edit button click should load edit Adresse page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Adresse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adressePageUrlPattern);
      });

      it.skip('edit button click should load edit Adresse page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Adresse');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adressePageUrlPattern);
      });

      it('last delete button click should delete instance of Adresse', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('adresse').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adressePageUrlPattern);

        adresse = undefined;
      });
    });
  });

  describe('new Adresse page', () => {
    beforeEach(() => {
      cy.visit(`${adressePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Adresse');
    });

    it('should create an instance of Adresse', () => {
      cy.get(`[data-cy="departmentName"]`).type('Soap Practical').should('have.value', 'Soap Practical');

      cy.get(`[data-cy="streetAddress"]`).type('payment Berkshire').should('have.value', 'payment Berkshire');

      cy.get(`[data-cy="postalCode"]`).type('Consultant Designer invoice').should('have.value', 'Consultant Designer invoice');

      cy.get(`[data-cy="city"]`).type('Rodriguezland').should('have.value', 'Rodriguezland');

      cy.get(`[data-cy="stateProvince"]`).type('white Movies online').should('have.value', 'white Movies online');

      cy.get(`[data-cy="pays"]`).type('Vanuatu Ukraine').should('have.value', 'Vanuatu Ukraine');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        adresse = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adressePageUrlPattern);
    });
  });
});
