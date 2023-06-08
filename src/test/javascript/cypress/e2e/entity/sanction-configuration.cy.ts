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

describe('SanctionConfiguration e2e test', () => {
  const sanctionConfigurationPageUrl = '/sanction-configuration';
  const sanctionConfigurationPageUrlPattern = new RegExp('/sanction-configuration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sanctionConfigurationSample = { codeAssociation: 'Huchette Finlande', codeTontine: 'b' };

  let sanctionConfiguration;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sanction-configurations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sanction-configurations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sanction-configurations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sanctionConfiguration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sanction-configurations/${sanctionConfiguration.id}`,
      }).then(() => {
        sanctionConfiguration = undefined;
      });
    }
  });

  it('SanctionConfigurations menu should load SanctionConfigurations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sanction-configuration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SanctionConfiguration').should('exist');
    cy.url().should('match', sanctionConfigurationPageUrlPattern);
  });

  describe('SanctionConfiguration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sanctionConfigurationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SanctionConfiguration page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sanction-configuration/new$'));
        cy.getEntityCreateUpdateHeading('SanctionConfiguration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionConfigurationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sanction-configurations',
          body: sanctionConfigurationSample,
        }).then(({ body }) => {
          sanctionConfiguration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sanction-configurations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sanction-configurations?page=0&size=20>; rel="last",<http://localhost/api/sanction-configurations?page=0&size=20>; rel="first"',
              },
              body: [sanctionConfiguration],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sanctionConfigurationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SanctionConfiguration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sanctionConfiguration');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionConfigurationPageUrlPattern);
      });

      it('edit button click should load edit SanctionConfiguration page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SanctionConfiguration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionConfigurationPageUrlPattern);
      });

      it.skip('edit button click should load edit SanctionConfiguration page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SanctionConfiguration');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionConfigurationPageUrlPattern);
      });

      it('last delete button click should delete instance of SanctionConfiguration', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sanctionConfiguration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sanctionConfigurationPageUrlPattern);

        sanctionConfiguration = undefined;
      });
    });
  });

  describe('new SanctionConfiguration page', () => {
    beforeEach(() => {
      cy.visit(`${sanctionConfigurationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SanctionConfiguration');
    });

    it('should create an instance of SanctionConfiguration', () => {
      cy.get(`[data-cy="codeAssociation"]`).type('systemic Handcrafted').should('have.value', 'systemic Handcrafted');

      cy.get(`[data-cy="codeTontine"]`).type('Pays').should('have.value', 'Pays');

      cy.get(`[data-cy="type"]`).select('ECHEC_TONTINE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sanctionConfiguration = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sanctionConfigurationPageUrlPattern);
    });
  });
});
