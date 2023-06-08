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

describe('DocumentAssociation e2e test', () => {
  const documentAssociationPageUrl = '/document-association';
  const documentAssociationPageUrlPattern = new RegExp('/document-association(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const documentAssociationSample = {};

  let documentAssociation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/document-associations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-associations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-associations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (documentAssociation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/document-associations/${documentAssociation.id}`,
      }).then(() => {
        documentAssociation = undefined;
      });
    }
  });

  it('DocumentAssociations menu should load DocumentAssociations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-association');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentAssociation').should('exist');
    cy.url().should('match', documentAssociationPageUrlPattern);
  });

  describe('DocumentAssociation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(documentAssociationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocumentAssociation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/document-association/new$'));
        cy.getEntityCreateUpdateHeading('DocumentAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentAssociationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/document-associations',
          body: documentAssociationSample,
        }).then(({ body }) => {
          documentAssociation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/document-associations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/document-associations?page=0&size=20>; rel="last",<http://localhost/api/document-associations?page=0&size=20>; rel="first"',
              },
              body: [documentAssociation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(documentAssociationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DocumentAssociation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('documentAssociation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentAssociationPageUrlPattern);
      });

      it('edit button click should load edit DocumentAssociation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentAssociationPageUrlPattern);
      });

      it.skip('edit button click should load edit DocumentAssociation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentAssociation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentAssociationPageUrlPattern);
      });

      it('last delete button click should delete instance of DocumentAssociation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('documentAssociation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentAssociationPageUrlPattern);

        documentAssociation = undefined;
      });
    });
  });

  describe('new DocumentAssociation page', () => {
    beforeEach(() => {
      cy.visit(`${documentAssociationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocumentAssociation');
    });

    it('should create an instance of DocumentAssociation', () => {
      cy.get(`[data-cy="codeDocument"]`).type('multi-byte recontextualize').should('have.value', 'multi-byte recontextualize');

      cy.get(`[data-cy="libele"]`).type('Baby input Gorgeous').should('have.value', 'Baby input Gorgeous');

      cy.get(`[data-cy="description"]`).type('Awesome feed cross-platform').should('have.value', 'Awesome feed cross-platform');

      cy.get(`[data-cy="dateEnregistrement"]`).type('2023-06-08').blur().should('have.value', '2023-06-08');

      cy.get(`[data-cy="dateArchivage"]`).type('2023-06-07').blur().should('have.value', '2023-06-07');

      cy.get(`[data-cy="version"]`).type('innovative').should('have.value', 'innovative');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        documentAssociation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', documentAssociationPageUrlPattern);
    });
  });
});
