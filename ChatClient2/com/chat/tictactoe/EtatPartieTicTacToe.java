package com.chat.tictactoe;

import observer.Observable;

public class EtatPartieTicTacToe  extends Observable {
    private char[][] etatPlateau = new char[3][3];

    public EtatPartieTicTacToe() {
        etatPlateau = new char[][]{
                {'.','.','.'},
                {'.','.','.'},
                {'.','.','.'}
        };
    }
    public boolean coup(String strCoup) {
        boolean res = false;
        if (strCoup !=null){
            String [] parties = strCoup.split(" ");
            if  (parties.length==3){
                char symbol = parties[0].charAt(0);
                int rangee = -1;
                int colonne = -1;

                try {

                    rangee = Integer.parseInt(parties[1]);
                    colonne = Integer.parseInt(parties[2]);


                    if (rangee >= 0 && rangee < 3 && colonne >= 0 && colonne < 3 && etatPlateau[rangee][colonne] == '.') {

                        etatPlateau[rangee][colonne] = symbol;
                        res = true;

                    }
                } catch (NumberFormatException e) {
                    res = false;

                }
            }
        }

        if (res){
            this.notifierObservateurs();
        }

        return res;
    }

    @Override
    public String toString() {
        String s = "";
        for (byte i=0;i<etatPlateau.length;i++) {
            for (int j=0;j<etatPlateau[i].length;j++)
                s+=etatPlateau[i][j]+" ";
            s+="\n";
        }
        return s;
    }

    public char[][] getEtatPlateau() {
        return etatPlateau;
    }

    public void setEtatPlateau(char[][] etatPlateau) {
        this.etatPlateau = etatPlateau;
    }
}
