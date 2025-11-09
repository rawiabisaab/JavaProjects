package com.atoudeft.tictactoe.classes;

import com.atoudeft.tictactoe.MethodeNonImplementeeException;

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
        // Si la partie est déjà terminée → on ne peut plus jouer
        if (statut != StatutPartie.EN_COURS)
            return false;

        // Vérifie que c’est bien le tour du joueur courant
        if (symbole != joueurCourant)
            return false;

        // Crée un coup et tente de le placer
        Coup coup = new Coup(position, symbole);
        boolean coupJoue = plateau.placer(coup);
        if (!coupJoue)
            return false; // case déjà occupée

        // Vérifie si la partie change de statut (victoire, nul, etc.)
        mettreAJourStatutApresCoup();

        // Si la partie continue, on change de joueur
        if (statut == StatutPartie.EN_COURS)
            joueurCourant = (joueurCourant == Symbole.X) ? Symbole.O : Symbole.X;

        return true;
    }


    public boolean isPartieEnCours() {
        if (statut != StatutPartie.EN_COURS) {
            return false;
        }
        return true;
    }
    private void mettreAJourStatutApresCoup() {
        var gagnante = plateau.ligneGagnante();
        if (!gagnante.isEmpty()) {
            Position p0 = gagnante.get(0);
            Symbole s = plateau.get(p0.getLigne(), p0.getColonne());
            statut = (s == Symbole.X) ? StatutPartie.X_GAGNE : StatutPartie.O_GAGNE;
            return;
        }
        if (plateau.estPlein()) {
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