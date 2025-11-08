package com.chat.serveur;

import com.commun.net.Connexion;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Cette classe étend (hérite) la classe abstraite Serveur et y ajoute le nécessaire pour que le
 * serveur soit un serveur de chat.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-15
 */
public class ServeurChat extends Serveur {

    /**
     * Crée un serveur de chat qui va écouter sur le port spécifié.
     *
     * @param port int Port d'écoute du serveur
     */
    Vector<String> historique = new Vector<String>();
    private List<Invitation> invitations = new ArrayList<>();
    private List<SalonPrive> salonsPrives = new ArrayList<>();
    public ServeurChat(int port) {
        super(port);
    }

    @Override
    public synchronized boolean ajouter(Connexion connexion) {
        String hist = this.historique();
        if ("".equals(hist)) {
            connexion.envoyer("OK");
        }
        else {
            connexion.envoyer("HIST " + hist);
        }
        return super.ajouter(connexion);
    }
    /**
     * Valide l'arrivée d'un nouveau client sur le serveur. Cette redéfinition
     * de la méthode héritée de Serveur vérifie si le nouveau client a envoyé
     * un alias composé uniquement des caractères a-z, A-Z, 0-9, - et _.
     *
     * @param connexion Connexion la connexion représentant le client
     * @return boolean true, si le client a validé correctement son arrivée, false, sinon
     */
    @Override
    protected boolean validerConnexion(Connexion connexion) {

        String aliasFourni = connexion.getAvailableText().trim();
        char c;
        int taille;
        boolean res = true;
        if ("".equals(aliasFourni)) {
            return false;
        }
        taille = aliasFourni.length();
        for (int i=0;i<taille;i++) {
            c = aliasFourni.charAt(i);
            if ((c<'a' || c>'z') && (c<'A' || c>'Z') && (c<'0' || c>'9')
                    && c!='_' && c!='-') {
                res = false;
                break;
            }
        }
        if (!res)
            return false;
        for (Connexion cnx:connectes) {
            if (aliasFourni.equalsIgnoreCase(cnx.getAlias())) { //alias déjà utilisé
                res = false;
                break;
            }
        }
        if (!res)
            return false;
        connexion.setAlias(aliasFourni);
        return true;
    }

    /**
     * Retourne la liste des alias des connectés au serveur dans une chaîne de caractères.
     *
     * @return String chaîne de caractères contenant la liste des alias des membres connectés sous la
     * forme alias1:alias2:alias3 ...
     */
    public String list() {
        String s = "";
        for (Connexion cnx:connectes)
            s+=cnx.getAlias()+":";
        return s;
    }
    /**
     * Retourne la liste des messages de l'historique de chat dans une chaîne
     * de caractères.
     *
     * @return String chaîne de caractères contenant la liste des alias des membres connectés sous la
     * forme message1\nmessage2\nmessage3 ...
     */
    public String historique() {
        String s = "";
        for (String msg : historique) {
            s += msg + "\n";
        }
        return s;
    }
    public void envoyerATousSauf(String str, String aliasExpediteur) {
        for(Connexion cnx:connectes) {
            if (!cnx.getAlias().equals(aliasExpediteur)) {
                cnx.envoyer(str);
            }
        }
    }
    public void envoyerA (String alias, String msg){
         for (Connexion cnx:connectes) {
             if (cnx.getAlias().equals(alias)) {
                 cnx.envoyer(msg);
             }
         }
    }

    public void ajouterHistorique(String msg) {
        historique.add(msg);
    }

    //Methodes de manipulation d'invitations

    public boolean existeInvitation(String host, String guest) {
        return invitations.contains(new Invitation(host, guest));
    }

    public void ajouterInvitation (String host, String guest) {
        Invitation inv = new Invitation(host, guest);
        invitations.add(inv);
    }

    public void supprimerInvitation(String host, String guest) {
        Invitation inv = new Invitation(host, guest);
        invitations.remove(inv);
    }
    public String listeInvitations(String alias){
        StringBuilder sb = new StringBuilder();
        for (Invitation inv:invitations){
            if (inv.getGuest().equals(alias)){
                sb.append(inv.getHost()).append(":");
            }
        }
        return sb.toString();
    }

    //Methodes de manipulation de salons
    public void creerSalonPrive(String alias1, String alias2) {
        salonsPrives.add(new SalonPrive(alias1, alias2));
    }

    public boolean existeSalon(String host, String guest) {
        SalonPrive salonRecherche = new SalonPrive(host, guest);
        return salonsPrives.contains(salonRecherche);
    }

    public boolean supprimerSalonPrive(SalonPrive salon) {
        return salonsPrives.remove(salon);
    }

}

