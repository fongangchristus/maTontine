import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'compte-tontine',
        data: { pageTitle: 'maTontineApp.compteTontine.home.title' },
        loadChildren: () => import('./compte-tontine/compte-tontine.module').then(m => m.CompteTontineModule),
      },
      {
        path: 'tontine',
        data: { pageTitle: 'maTontineApp.tontine.home.title' },
        loadChildren: () => import('./tontine/tontine.module').then(m => m.TontineModule),
      },
      {
        path: 'banque',
        data: { pageTitle: 'maTontineApp.banque.home.title' },
        loadChildren: () => import('./banque/banque.module').then(m => m.BanqueModule),
      },
      {
        path: 'compte-banque',
        data: { pageTitle: 'maTontineApp.compteBanque.home.title' },
        loadChildren: () => import('./compte-banque/compte-banque.module').then(m => m.CompteBanqueModule),
      },
      {
        path: 'cotisation-banque',
        data: { pageTitle: 'maTontineApp.cotisationBanque.home.title' },
        loadChildren: () => import('./cotisation-banque/cotisation-banque.module').then(m => m.CotisationBanqueModule),
      },
      {
        path: 'decaisement-banque',
        data: { pageTitle: 'maTontineApp.decaisementBanque.home.title' },
        loadChildren: () => import('./decaisement-banque/decaisement-banque.module').then(m => m.DecaisementBanqueModule),
      },
      {
        path: 'paiement-banque',
        data: { pageTitle: 'maTontineApp.paiementBanque.home.title' },
        loadChildren: () => import('./paiement-banque/paiement-banque.module').then(m => m.PaiementBanqueModule),
      },
      {
        path: 'gestionnaire-banque',
        data: { pageTitle: 'maTontineApp.gestionnaireBanque.home.title' },
        loadChildren: () => import('./gestionnaire-banque/gestionnaire-banque.module').then(m => m.GestionnaireBanqueModule),
      },
      {
        path: 'gestionnaire-tontine',
        data: { pageTitle: 'maTontineApp.gestionnaireTontine.home.title' },
        loadChildren: () => import('./gestionnaire-tontine/gestionnaire-tontine.module').then(m => m.GestionnaireTontineModule),
      },
      {
        path: 'cotisation-tontine',
        data: { pageTitle: 'maTontineApp.cotisationTontine.home.title' },
        loadChildren: () => import('./cotisation-tontine/cotisation-tontine.module').then(m => m.CotisationTontineModule),
      },
      {
        path: 'session-tontine',
        data: { pageTitle: 'maTontineApp.sessionTontine.home.title' },
        loadChildren: () => import('./session-tontine/session-tontine.module').then(m => m.SessionTontineModule),
      },
      {
        path: 'decaissement-tontine',
        data: { pageTitle: 'maTontineApp.decaissementTontine.home.title' },
        loadChildren: () => import('./decaissement-tontine/decaissement-tontine.module').then(m => m.DecaissementTontineModule),
      },
      {
        path: 'paiement-tontine',
        data: { pageTitle: 'maTontineApp.paiementTontine.home.title' },
        loadChildren: () => import('./paiement-tontine/paiement-tontine.module').then(m => m.PaiementTontineModule),
      },
      {
        path: 'paiement',
        data: { pageTitle: 'maTontineApp.paiement.home.title' },
        loadChildren: () => import('./paiement/paiement.module').then(m => m.PaiementModule),
      },
      {
        path: 'association',
        data: { pageTitle: 'maTontineApp.association.home.title' },
        loadChildren: () => import('./association/association.module').then(m => m.AssociationModule),
      },
      {
        path: 'monnaie',
        data: { pageTitle: 'maTontineApp.monnaie.home.title' },
        loadChildren: () => import('./monnaie/monnaie.module').then(m => m.MonnaieModule),
      },
      {
        path: 'document-association',
        data: { pageTitle: 'maTontineApp.documentAssociation.home.title' },
        loadChildren: () => import('./document-association/document-association.module').then(m => m.DocumentAssociationModule),
      },
      {
        path: 'exercise',
        data: { pageTitle: 'maTontineApp.exercise.home.title' },
        loadChildren: () => import('./exercise/exercise.module').then(m => m.ExerciseModule),
      },
      {
        path: 'adresse',
        data: { pageTitle: 'maTontineApp.adresse.home.title' },
        loadChildren: () => import('./adresse/adresse.module').then(m => m.AdresseModule),
      },
      {
        path: 'fonction',
        data: { pageTitle: 'maTontineApp.fonction.home.title' },
        loadChildren: () => import('./fonction/fonction.module').then(m => m.FonctionModule),
      },
      {
        path: 'fonction-adherent',
        data: { pageTitle: 'maTontineApp.fonctionAdherent.home.title' },
        loadChildren: () => import('./fonction-adherent/fonction-adherent.module').then(m => m.FonctionAdherentModule),
      },
      {
        path: 'adhesion',
        data: { pageTitle: 'maTontineApp.adhesion.home.title' },
        loadChildren: () => import('./adhesion/adhesion.module').then(m => m.AdhesionModule),
      },
      {
        path: 'paiement-adhesion',
        data: { pageTitle: 'maTontineApp.paiementAdhesion.home.title' },
        loadChildren: () => import('./paiement-adhesion/paiement-adhesion.module').then(m => m.PaiementAdhesionModule),
      },
      {
        path: 'formule-adhesion',
        data: { pageTitle: 'maTontineApp.formuleAdhesion.home.title' },
        loadChildren: () => import('./formule-adhesion/formule-adhesion.module').then(m => m.FormuleAdhesionModule),
      },
      {
        path: 'personne',
        data: { pageTitle: 'maTontineApp.personne.home.title' },
        loadChildren: () => import('./personne/personne.module').then(m => m.PersonneModule),
      },
      {
        path: 'fiche-presence',
        data: { pageTitle: 'maTontineApp.fichePresence.home.title' },
        loadChildren: () => import('./fiche-presence/fiche-presence.module').then(m => m.FichePresenceModule),
      },
      {
        path: 'presence',
        data: { pageTitle: 'maTontineApp.presence.home.title' },
        loadChildren: () => import('./presence/presence.module').then(m => m.PresenceModule),
      },
      {
        path: 'contact',
        data: { pageTitle: 'maTontineApp.contact.home.title' },
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule),
      },
      {
        path: 'compte-rib',
        data: { pageTitle: 'maTontineApp.compteRIB.home.title' },
        loadChildren: () => import('./compte-rib/compte-rib.module').then(m => m.CompteRIBModule),
      },
      {
        path: 'historique-personne',
        data: { pageTitle: 'maTontineApp.historiquePersonne.home.title' },
        loadChildren: () => import('./historique-personne/historique-personne.module').then(m => m.HistoriquePersonneModule),
      },
      {
        path: 'assemble',
        data: { pageTitle: 'maTontineApp.assemble.home.title' },
        loadChildren: () => import('./assemble/assemble.module').then(m => m.AssembleModule),
      },
      {
        path: 'evenement',
        data: { pageTitle: 'maTontineApp.evenement.home.title' },
        loadChildren: () => import('./evenement/evenement.module').then(m => m.EvenementModule),
      },
      {
        path: 'pot',
        data: { pageTitle: 'maTontineApp.pot.home.title' },
        loadChildren: () => import('./pot/pot.module').then(m => m.PotModule),
      },
      {
        path: 'type-pot',
        data: { pageTitle: 'maTontineApp.typePot.home.title' },
        loadChildren: () => import('./type-pot/type-pot.module').then(m => m.TypePotModule),
      },
      {
        path: 'contribution-pot',
        data: { pageTitle: 'maTontineApp.contributionPot.home.title' },
        loadChildren: () => import('./contribution-pot/contribution-pot.module').then(m => m.ContributionPotModule),
      },
      {
        path: 'commentaire-pot',
        data: { pageTitle: 'maTontineApp.commentairePot.home.title' },
        loadChildren: () => import('./commentaire-pot/commentaire-pot.module').then(m => m.CommentairePotModule),
      },
      {
        path: 'type-evenement',
        data: { pageTitle: 'maTontineApp.typeEvenement.home.title' },
        loadChildren: () => import('./type-evenement/type-evenement.module').then(m => m.TypeEvenementModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'maTontineApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'sanction-configuration',
        data: { pageTitle: 'maTontineApp.sanctionConfiguration.home.title' },
        loadChildren: () => import('./sanction-configuration/sanction-configuration.module').then(m => m.SanctionConfigurationModule),
      },
      {
        path: 'sanction',
        data: { pageTitle: 'maTontineApp.sanction.home.title' },
        loadChildren: () => import('./sanction/sanction.module').then(m => m.SanctionModule),
      },
      {
        path: 'paiement-sanction',
        data: { pageTitle: 'maTontineApp.paiementSanction.home.title' },
        loadChildren: () => import('./paiement-sanction/paiement-sanction.module').then(m => m.PaiementSanctionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
