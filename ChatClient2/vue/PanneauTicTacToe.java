package vue;

import com.chat.tictactoe.EtatPartieTicTacToe;
import controleur.EcouteurTicTacToe;
import observer.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanneauTicTacToe extends JPanel {
    private JButton[][] boutons = new JButton[3][3];
    private EtatPartieTicTacToe partie;
    private ActionListener ecouteurTicTacToe;

    public PanneauTicTacToe(EtatPartieTicTacToe partie) {
        this.partie = partie;
        char[][] etatPlateau = partie.getEtatPlateau();
        this.setLayout(new GridLayout(3,3));
        ecouteurTicTacToe = new EcouteurTicTacToe(null);
        for (int i=0;i<boutons.length;i++)
            for (int j=0;j<boutons[i].length;j++) {
                boutons[i][j] = new JButton();
                boutons[i][j].setActionCommand(i+""+j);
                boutons[i][j].setBackground(Color.WHITE);
                this.add(boutons[i][j]);
                if (etatPlateau[i][j]!='.')
                    boutons[i][j].setIcon(ServiceImages.getIconePourSymbole(etatPlateau[i][j]));
            }
        //Connecter l'observateur sur l'observable :
        //partie.ajouterObservateur(this);
    }
    public void setEcouteurTicTacToe(ActionListener ecouteurTicTacToe) {
        this.ecouteurTicTacToe = ecouteurTicTacToe;
        for (int i=0;i<boutons.length;i++)
            for (int j=0;j<boutons[i].length;j++)
                boutons[i][j].addActionListener(ecouteurTicTacToe);
    }
}