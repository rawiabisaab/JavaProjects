package com.atoudeft.tictactoe.classes;

import java.util.Objects;
public final class Position {
    private final int ligne;
    private final int colonne;
    public Position(int ligne, int colonne) {
        if (ligne < 0 || ligne > 2 || colonne < 0 || colonne > 2) {
            throw new IllegalArgumentException("Position hors plateau: (" + ligne + "," + colonne + ")");
        }
        this.ligne = ligne;
        this.colonne = colonne;
    }
    public int getLigne()   { return ligne; }
    public int getColonne() { return colonne; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return ligne == p.ligne && colonne == p.colonne;
    }
    @Override public int hashCode() { return Objects.hash(ligne, colonne); }
    @Override public String toString() { return "(" + ligne + "," + colonne + ")"; }
}