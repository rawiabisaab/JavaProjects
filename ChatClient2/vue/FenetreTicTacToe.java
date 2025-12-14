package vue;

import javax.swing.*;

public class FenetreTicTacToe extends JFrame {

    PanneauTicTacToe panneauTicTacToe;

    public FenetreTicTacToe(PanneauTicTacToe panneauTicTacToe, String titre) {
        this.panneauTicTacToe = panneauTicTacToe;
        this.setTitle(titre);
        initialiserComposants();
        configurerFenetrePrincipale();
    }

    private void configurerFenetrePrincipale() {
        //Configuration de la fenêtre
        this.setSize(800,800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initialiserComposants() {
        //Création et initialisation des composants
        this.add(panneauTicTacToe);
    }
}