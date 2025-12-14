package vue;

import javax.swing.*;
import java.awt.*;

public class PanneauChatPrive extends PanneauChat {

    private JButton btnInviter, btnRefuser;

    public PanneauChatPrive() {
        super();
        JPanel nord = new JPanel();
        btnInviter = new JButton("Inviter TTT");
        btnRefuser = new JButton("Refuser");
        btnRefuser.setVisible(false);
        nord.add(btnInviter);
        nord.add(btnRefuser);
        add(nord, BorderLayout.NORTH);
    }

    public JButton getBtnInviter() { return btnInviter; }
    public JButton getBtnRefuser() { return btnRefuser; }

    public void invitationAJouerRecue() {
        btnInviter.setText("Accepter");
        btnRefuser.setVisible(true);
    }

    public void invitationAJouerAnnulee() {
        btnInviter.setText("Inviter TTT");
        btnRefuser.setVisible(false);
    }
}