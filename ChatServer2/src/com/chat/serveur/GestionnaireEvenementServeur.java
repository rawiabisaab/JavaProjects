package com.chat.serveur;

import com.atoudeft.tictactoe.classes.Partie;
import com.atoudeft.tictactoe.classes.StatutPartie;
import com.atoudeft.tictactoe.classes.Symbole;
import com.commun.evenement.Evenement;
import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;
import com.atoudeft.tictactoe.classes.Position;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx, cnx2;
        String msg, typeEvenement, aliasExpediteur, aliasDestinataire, str, s1, s2;
        String hote, invite;
        ServeurChat serveurChat = (ServeurChat) this.serveur;
        SalonPrive sp;
        Partie partie;
        char charSymbole, posX, posY;
        Symbole symbole;
        StatutPartie statut;
        Position pos1, pos2;
        int i, j;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu de "+cnx.getAlias()+" : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
    /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    aliasExpediteur = cnx.getAlias();
                    serveurChat.envoyerATousSauf("EXIT "+aliasExpediteur, aliasExpediteur);
                    serveurChat.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connectées :
                    cnx.envoyer("LIST " + serveurChat.list());
                    break;
    /******************* CHAT PUBLIC *******************/
                case "MSG": //Ajoute le message recu à l'historique du salon public
                    //et l'envoie à tous les connectés sauf l'expéditeur :
                    aliasExpediteur = cnx.getAlias();
                    msg = aliasExpediteur + ">>" + evenement.getArgument();
                    serveurChat.envoyerATousSauf("MSG "+msg, aliasExpediteur);
                    serveurChat.ajouterHistorique(msg);
                    break;
                //Ajoutez ici d’autres case pour gérer d’autres commandes.
    /******************* CHAT PRIVÉ *******************/
                case "JOIN" : //Recoit une invitation ou acceptation d'invitation
                    hote = cnx.getAlias();
                    invite = evenement.getArgument().trim();
                    if ("".equals(invite))  //invité non indiqué.
                        break;
                    cnx2 = serveurChat.getConnexionParAlias(invite);
                    if (cnx2==null) //l'invité n'est pas connecté.
                        break;
                    if (serveurChat.enPrive(hote,invite)) //déjà en chat privé.
                        break;
                    int result = serveurChat.traiterInvitation(hote,invite);
                    if (result==Invitation.ACCEPTEE) { //créer salon privé
                        serveurChat.ajouter(new SalonPrive(hote,invite));
                        serveurChat.supprimeInvitation(hote,invite);
                        cnx.envoyer("JOINOK "+invite);
                        cnx2.envoyer("JOINOK "+hote);
                    }
                    else if (result==Invitation.AJOUTEE) { //informer l'invité
                        cnx2.envoyer("JOIN "+hote);
                    }
                    break;
                case "DECLINE" :  //refuse une invitation à un chat ou à un jeu de Tic-tac-Toe
                    aliasExpediteur = cnx.getAlias();
                    aliasDestinataire = evenement.getArgument().trim();
                    if ("".equals(aliasDestinataire))  //destinataire non indiqué.
                        break;
                    cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                    if (cnx2==null) //le destinataire n'est pas connecté.
                        break;
                    if (serveurChat.supprimeInvitation(aliasDestinataire,aliasExpediteur)) {
                        cnx2.envoyer("DECLINE "+aliasExpediteur);
                    }
                    else if (serveurChat.supprimeInvitationTicTacToe(aliasDestinataire,aliasExpediteur))/*************************/
                        cnx2.envoyer("DECLINE_TTT "+aliasExpediteur);
                    break;
                case "INV" :  //envoyer liste des invitations reçues
                    cnx.envoyer("INV " + serveurChat.listInvitations(cnx.getAlias()));
                    break;
                case "PRV" :  //envoyer un message privé
                    msg = evenement.getArgument();
                    i = msg.indexOf(' ');
                    if (i == -1) //message vide
                        break;
                    else {
                        aliasExpediteur = cnx.getAlias();
                        aliasDestinataire = msg.substring(0, i);
                        if (!serveurChat.enPrive(aliasExpediteur,aliasDestinataire))
                            break;
                        cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                        if (cnx2!=null)
                            cnx2.envoyer("PRV "+aliasExpediteur+" "+msg.substring(i).trim());
                    }
                    break;
                case "QUIT" : //quitter un chat privé
                    aliasExpediteur = cnx.getAlias();
                    aliasDestinataire = evenement.getArgument().trim();
                    if ("".equals(aliasDestinataire))  //hote non indiqué.
                        break;
                    if (serveurChat.supprimeSalonPrive(aliasExpediteur,aliasDestinataire)) {
                        cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                        if (cnx2==null) //l'hote n'est pas connecté.
                            break;
                        cnx.envoyer("QUIT "+aliasDestinataire);
                        cnx2.envoyer("QUIT "+aliasExpediteur);
                    }
                    break;
    /******************* JEU DE Tic-Tac-Toe EN RÉSEAU *******************/
                case "TTT" : //Réception d'une invitation à jouer ou acceptation d'une invitation
                    aliasExpediteur = cnx.getAlias();
                    aliasDestinataire = evenement.getArgument().trim();
                    if ("".equals(aliasDestinataire))  //hote non indiqué.
                        break;
                    //Vérifier que les 2 ne sont pas déjà dans une partie :
                    if (serveurChat.getPartieDe(aliasExpediteur)!=null || serveurChat.getPartieDe(aliasDestinataire)!=null)
                        break;
                    //Vérifier que les 2 sont en chat privé :
                    if (serveurChat.enPrive(aliasExpediteur,aliasDestinataire)) {
                        sp = serveurChat.getSalonPrive(aliasExpediteur, aliasDestinataire);  //on accède au salon privé
                        if (sp!=null) { //Le salon privé avec les 2 personnes existe
                            if (sp.getPartie()!=null) { //les 2 sont deja en partie ensemble
                                break;
                            }
                            partie = sp.addJoueur(aliasExpediteur);
                            cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                            if (partie!=null) { //c'est le 2e joueur qui est ajouté:on a démarre une partie
                                cnx.envoyer("TTTOK "+aliasDestinataire+" "+sp.getSymbole(aliasExpediteur));
                                cnx2.envoyer("TTTOK "+aliasExpediteur+" "+sp.getSymbole(aliasDestinataire));
                                sp.supprimeInvitationATicTacToe();
                            }
                            else {
                                cnx2.envoyer("TTT "+aliasExpediteur);
                            }
                        }
                    }
                    break;
                case "COUP" : //Coup de Tic-Tac-Toe
                    aliasExpediteur = cnx.getAlias();
                    partie = serveurChat.getPartieDe(aliasExpediteur);
                    if (partie!=null) {
                        aliasDestinataire = serveurChat.getJoueurAdverseDe(aliasExpediteur);
                        cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                        str = evenement.getArgument().trim();
                        if (str.length()<5) //Format de str attendu : X 2 1
                            cnx.envoyer("INVALID mauvais format");
                        else {
                            str = str.substring(0,5).toUpperCase();
                            //On recupere le symbole du coup:
                            charSymbole = str.charAt(0);
                            if (charSymbole=='X')
                                symbole = Symbole.X;
                            else if (charSymbole=='O')
                                symbole = Symbole.O;
                            else {
                                cnx.envoyer("INVALID mauvais format");
                                break;
                            }
                            if (symbole != partie.getJoueurCourant()) {
                                cnx.envoyer("INVALID Ce n'est pas votre tour");
                                break;
                            }
                            //On recupere la position du coup :
                            posX = str.charAt(2);
                            posY = str.charAt(4);
                            i = (byte)Character.getNumericValue(posX);
                            j = (byte)Character.getNumericValue(posY);
                            //On tente le coup :
                            if (!partie.jouer(symbole,new Position(i,j))) {
                                cnx.envoyer("INVALID Coup invalide");
                                break;
                            }
                            //Le coup est valide.
                            if (!partie.isPartieEnCours()) {//Partie terminee
                                statut = partie.getStatut();
                                if (statut==StatutPartie.NULLE) { //partie nulle
                                    cnx.envoyer("TTT_END Parie nulle");
                                    cnx2.envoyer("TTT_END Parie nulle");
                                }
                                else {
                                    sp = serveurChat.getSalonPrive(aliasExpediteur, aliasDestinataire);  //on accède au salon privé
                                    if (statut == StatutPartie.X_GAGNE) { //l'hote a gagné
                                        cnx.envoyer("TTT_END "+sp.getHote()+" a gagne");
                                        cnx2.envoyer("TTT_END "+sp.getHote()+" a gagne");
                                    }
                                    else { //L'invite a gagné
                                        cnx.envoyer("TTT_END "+sp.getInvite()+" a gagne");
                                        cnx2.envoyer("TTT_END "+sp.getInvite()+" a gagne");
                                    }
                                }
                            }
                            else { //Coup valide
                                System.out.println(partie);
                                cnx.envoyer("COUP " + str);
                                cnx2.envoyer("COUP " + str);
                            }
                        }
                    }
                    break;
                case "ABANDON":
                    aliasExpediteur = cnx.getAlias();
                    partie = serveurChat.getPartieDe(aliasExpediteur);
                    if (partie!=null) {
                        aliasDestinataire = serveurChat.getJoueurAdverseDe(aliasExpediteur);
                        cnx2 = serveurChat.getConnexionParAlias(aliasDestinataire);
                        if (cnx2!=null) //être sûr que l'autre joueur n'est pas parti.
                            cnx2.envoyer("ABANDON "+aliasExpediteur+" a abandonné la partie. Vous avez gagné");
                        cnx.envoyer("ABANDON Vous avez abandonné la partie. "+aliasDestinataire+" a gagné.");
                        sp = serveurChat.getSalonPrive(aliasExpediteur,aliasDestinataire);
                        if (sp!=null)
                            sp.setPartie(null);//on détruit la partie de Tic-Tac-Toe.
                    }
                    break;
    /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}