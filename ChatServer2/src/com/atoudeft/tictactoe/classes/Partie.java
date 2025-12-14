package com.atoudeft.tictactoe.classes;

import java.util.List;
public final class Partie {
    private final Plateau plateau = new Plateau();
    private Symbole joueurCourant;
    private StatutPartie statut;
    public Plateau getPlateau()       {
        return plateau;
    }
    public Symbole getJoueurCourant() {
       return joueurCourant;
    }
    public StatutPartie getStatut()   {
        return statut;
    }

    public Partie(Symbole joueurCourant) {
        this.joueurCourant = joueurCourant;
        statut = StatutPartie.EN_COURS;
    }
    public Partie() {
        this(Symbole.X);
    }

    public boolean jouer(Symbole symbole, Position position) {
        if (!isPartieEnCours())
            return false;
        if (joueurCourant!=symbole)
            return false;
        if (!plateau.estVide(position)) {
            return false;
        }
        plateau.placer(new Coup(position, joueurCourant));
        mettreAJourStatutApresCoup();
        if (statut == StatutPartie.EN_COURS) {
            joueurCourant = (joueurCourant == Symbole.X) ? Symbole.O : Symbole.X;
        }
        return true;
    }
    public boolean isPartieEnCours() {
        if (statut != StatutPartie.EN_COURS) {
            return false;
        }
        return true;
    }
    private void mettreAJourStatutApresCoup() {
        List<Position> ligne = plateau.ligneGagnante();
        if (!ligne.isEmpty()) {
            statut = (joueurCourant == Symbole.X) ? StatutPartie.X_GAGNE : StatutPartie.O_GAGNE;
        } else if (plateau.estPlein()) {
            statut = StatutPartie.NULLE;
        }
    }

    @Override
    public String toString() {
        String str = "";
        str = plateau +"\n"
                +"Joueur Courant : " + joueurCourant +"\n"
                +"Etat : " + statut + "\n";
        return str;
    }
}