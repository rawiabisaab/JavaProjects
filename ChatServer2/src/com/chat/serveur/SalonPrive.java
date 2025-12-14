package com.chat.serveur;

import com.atoudeft.tictactoe.classes.Partie;
import com.atoudeft.tictactoe.classes.Symbole;

import java.util.Objects;

public class SalonPrive {
    private String hote, invite;
    private Partie partie;
    private String aliasPremierJoueur;

    public SalonPrive(String hote, String invite) {
        this.hote = hote;
        this.invite = invite;
    }

    public Partie getPartie() {
        return partie;
    }
    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public String getHote() {
        return hote;
    }
    public String getInvite() {
        return invite;
    }

    /**
     * Definit l'egalite entre 2 salons prives. Deux salons sont egaux s'ils
     * contiennent les memes personnes, peu importe leurs roles.
     *
     * @param obj Object avec lequel on compare this.
     * @return boolean true, si les 2 salons ont les memes personnes, false, sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SalonPrive that = (SalonPrive) obj;
        return hote.equals(that.hote) && invite.equals(that.invite)
            || hote.equals(that.invite) && invite.equals(that.hote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hote, invite);
    }

    public Partie addJoueur(String alias) {
        if (partie!=null) { //Une partie est deja en cours
            return partie;
        }
        if (aliasPremierJoueur==null) {
            //on mémorise le 1er joueur mais on n'a pas encore de partie de Tic-Tac-Toe :
            aliasPremierJoueur = alias;
            return null;
        }
        else if (!alias.equals(aliasPremierJoueur)) {
            //On a le 2e joueur donc on démarre la partie :
            partie = new Partie();
            return partie;
        }
        else
            return null;
    }
    //L'hote joue toujours avec X et l'invité avec O:
    public Symbole getSymbole(String alias) {
        if (alias.equals(hote)) {
            return Symbole.X;
        }
        else if (alias.equals(invite)) {
            return Symbole.O;
        }
        return null;
    }
    public boolean supprimeInvitationATicTacToe() {
        if (aliasPremierJoueur!=null) {
            this.aliasPremierJoueur = null;
            return true;
        }
        return false;
    }
}