package com.chat.client;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.chat.tictactoe.EtatPartieTicTacToe;

/**
 * Cette classe représente un gestionnaire d'événement d'un client. Lorsqu'un client reçoit un texte d'un serveur,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementClient implements GestionnaireEvenement {
    private Client client;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient(Client client) {
        this.client = client;
    }
    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un serveur.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String typeEvenement, arg, str;
        String[] membres, invAlias;
        EtatPartieTicTacToe etat;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
    /******************* COMMANDES GÉNÉRALES *******************/
                case "END" : //Le serveur demande de fermer la connexion
                    client.deconnecter(); //On ferme la connexion
                    break;
                case "LIST" : //Le serveur a renvoyé la liste des connectés
                    arg = evenement.getArgument();
                    membres = arg.split(":");
                    System.out.println("\t\t"+membres.length+" personnes dans le salon :");
                    for (String s:membres)
                        System.out.println("\t\t\t- "+s);
                    break;
    /******************* CHAT PUBLIC *******************/
                case "HIST" : //Le serveur a renvoyé l'historique des messages du chat public
                    arg = evenement.getArgument();
                    membres = arg.split("\n");
                    for (String s:membres)
                        System.out.println("\t\t\t."+s);
                    break;
    /******************* CHAT PRIVÉ *******************/
                case "JOIN" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " vous a envoyé une invitation à un chat privé (JOIN/DECLINE alias " +
                            "pour accepter ou refuser)");
                    break;
                case "JOINOK" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " Vous êtes en chat privé avec "+arg+" (PRV alias msg pour lui envoyer " +
                            "un message en privé)");
                    break;
                case "DECLINE" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " a refuse/annule l'invitation a chatter en prive.");
                    break;
                case "INV" : //Le serveur a renvoyé la liste des invitations reçues
                    arg = evenement.getArgument();
                    invAlias = arg.split(":");
                    System.out.println("\t\tInvitations reçues :");
                    for (String s:invAlias)
                        System.out.println("\t\t\t- "+s);
                    break;
                case "QUIT" :
                    arg = evenement.getArgument();
                    System.out.println(arg +" a quitté le salon privé.");
                    break;
   /******************* JEU D'ÉCHECS EN RÉSEAU *******************/
                case "CHESSOK":
                    arg = evenement.getArgument();
                    str = arg.substring(arg.indexOf(" ")+1);
                    arg = arg.substring(0,arg.indexOf(" "));
                    ((ClientChat)client).nouvellePartie();
                    System.out.println("Partie d'échecs démarrée avec "+arg+". Votre couleur est : "+str);
                    System.out.println(((ClientChat)client).getEtatPartieTicTacToe());
                    break;
                case "INVALID":
                    System.out.println("Mouvement invalide");
                    break;
                case "MOVE":
                    etat = ((ClientChat)client).getEtatPartieTicTacToe();
                    arg = evenement.getArgument();
                    if (etat.coup(arg)) {
                        System.out.println(etat);
                    }
                    else //Ne devrait jamais rentrer dans le else car le serveur a validé le déplacement.
                        System.out.println("DEPLACEMENT BIZARRE : "+arg);
                    break;
   /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Afficher le texte recu :
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
}
