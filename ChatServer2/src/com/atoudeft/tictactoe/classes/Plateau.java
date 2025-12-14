package com.atoudeft.tictactoe.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class Plateau {
    private final Symbole[][] grille = new Symbole[3][3];
    private int casesRemplies = 0;
    public Symbole get(int ligne, int colonne) { return grille[ligne][colonne]; }
    public boolean estVide(Position p) { return grille[p.getLigne()][p.getColonne()] == null; }
    public int getNombreCasesRemplies() { return casesRemplies; }
    public boolean estPlein() { return casesRemplies == 9; }
    public boolean placer(Coup coup) {
        Position p = coup.getPosition();
        if (grille[p.getLigne()][p.getColonne()] != null) {
            return false;
        }
        grille[p.getLigne()][p.getColonne()] = coup.getSymbole();
        casesRemplies++;
        return true;
    }
    public List<Position> ligneGagnante() {
        int[][][] lignes = {
            {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}},
            {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}},
            {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}}
        };
        for (int[][] l : lignes) {
            Symbole a = grille[l[0][0]][l[0][1]];
            Symbole b = grille[l[1][0]][l[1][1]];
            Symbole c = grille[l[2][0]][l[2][1]];
            if (a != null && a == b && b == c) {
                List<Position> res = new ArrayList<>(3);
                res.add(new Position(l[0][0], l[0][1]));
                res.add(new Position(l[1][0], l[1][1]));
                res.add(new Position(l[2][0], l[2][1]));
                return res;
            }
        }
        return Collections.emptyList();
    }
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                sb.append(grille[r][c] == null ? "." : grille[r][c]);
                if (c < 2) sb.append(" ");
            }
            if (r < 2) sb.append("\n");
        }
        return sb.toString();
    }
}