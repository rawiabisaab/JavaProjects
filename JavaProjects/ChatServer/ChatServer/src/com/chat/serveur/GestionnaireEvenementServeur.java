package com.chat.serveur;

import com.commun.evenement.Evenement;
import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;

/**
 * Cette classe repr�sente un gestionnaire d'�v�nement d'un serveur. Lorsqu'un serveur re�oit un texte d'un client,
 * il cr�e un �v�nement � partir du texte re�u et alerte ce gestionnaire qui r�agit en g�rant l'�v�nement.
 *
 * @author Abdelmoum�ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    /**
     * Construit un gestionnaire d'�v�nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire g�re des �v�nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * M�thode de gestion d'�v�nements. Cette m�thode contiendra le code qui g�re les r�ponses obtenues d'un client.
     *
     * @param evenement L'�v�nement � g�rer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoy� "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connect�es :
                    cnx.envoyer("LIST " + serveur.list());
                    break;

                case "MSG": //Traite les commandes des clients
                    String texteMessage = evenement.getArgument();
                    aliasExpediteur = cnx.getAlias();
                    String messageEnvoye = aliasExpediteur + ">> " + texteMessage;
                    serveur.envoyerATousSauf(messageEnvoye, aliasExpediteur);
                    serveur.ajouterHistorique(messageEnvoye);
                    break;

                case "JOIN" : //Traite les invitations

                    aliasExpediteur = cnx.getAlias();
                    String alias2 = evenement.getArgument();

                    if (serveur.existeInvitation(alias2, aliasExpediteur)) {

                        serveur.supprimerInvitation(alias2, aliasExpediteur);

                        serveur.creerSalonPrive(aliasExpediteur, alias2);

                        serveur.envoyerA(aliasExpediteur, "Salon privé avec : " + alias2);
                        serveur.envoyerA(alias2, "Salon avec : " + aliasExpediteur);
                }
                    else {

                        serveur.ajouterInvitation(aliasExpediteur, alias2);

                        serveur.envoyerA(alias2, "Invitation de : " + aliasExpediteur);

                    }

                    break;

                case "DECLINE" :
                   String alias1 = cnx.getAlias();
                   alias2 = evenement.getArgument();

                   if (serveur.existeInvitation(alias2, alias1)) {
                       serveur.supprimerInvitation(alias2, alias1);
                       serveur.envoyerA(alias2, "Invitation avec : " + alias1 + " refusée");
                   }  else if (serveur.existeInvitation(alias1, alias2)) {
                       serveur.supprimerInvitation(alias1, alias2);
                       serveur.envoyerA(alias1, "Invitation annulée");
                   }



                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}