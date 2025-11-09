package com.atoudeft.tictactoe.programme;

import com.atoudeft.tictactoe.classes.*;


import java.util.Locale;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        Scanner sc = new Scanner(System.in);
        Partie partie = new Partie();

        System.out.println(partie); // doit afficher plateau + Joueur Courant + Etat
        System.out.println();
        System.out.println("Entrez des coups au format:  X 0 2");
        System.out.println("Commandes:  aide   q");
        System.out.println("(Vous pouvez coller plusieurs coups d’un coup, même sur plusieurs lignes.)");

        while (partie.getStatut() == StatutPartie.EN_COURS) {
            System.out.print("\n> ");

            if (!sc.hasNext()) break;                // fin de flux (collage terminé)
            String tok = sc.next();

            // normaliser quelques espaces/char exotiques éventuels
            tok = tok.replace('\u00A0', ' ')
                    .replace('\u2007', ' ')
                    .replace('\u202F', ' ')
                    .trim();

            if (tok.equalsIgnoreCase("q")) {
                System.out.println("Fin.");
                return;
            }
            if (tok.equalsIgnoreCase("aide")) {
                System.out.println("Format d’un coup:  symbole ligne colonne  (ex: X 0 2)");
                System.out.println("symbole ∈ {X,O}, ligne ∈ {0,1,2}, colonne ∈ {0,1,2}");
                continue;
            }

            // 1er jeton = symbole
            Symbole s;
            try {
                s = Symbole.valueOf(tok.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                System.out.println("Mauvaise saisie (symbole attendu: X ou O).");
                // vider le reste de la ligne pour repartir proprement
                if (sc.hasNextLine()) sc.nextLine();
                continue;
            }

            // 2e et 3e jetons = deux entiers 0..2
            if (!sc.hasNextInt()) {
                System.out.println("Mauvaise saisie (ligne attendue: entier 0..2).");
                if (sc.hasNextLine()) sc.nextLine();
                continue;
            }
            int r = sc.nextInt();

            if (!sc.hasNextInt()) {
                System.out.println("Mauvaise saisie (colonne attendue: entier 0..2).");
                if (sc.hasNextLine()) sc.nextLine();
                continue;
            }
            int c = sc.nextInt();

            if (r < 0 || r > 2 || c < 0 || c > 2) {
                System.out.println("Hors limites (utilisez 0, 1 ou 2).");
                continue;
            }

            boolean ok = partie.jouer(s, new Position(r, c));
            if (!ok) {
                System.out.println("Coup refusé (mauvais joueur, case occupée ou partie terminée).");
                continue;
            }

            // Affiche l’état après ce coup
            System.out.println();
            System.out.println(partie);
        }

        System.out.println("\nStatut final: " + partie.getStatut());
    }
}
