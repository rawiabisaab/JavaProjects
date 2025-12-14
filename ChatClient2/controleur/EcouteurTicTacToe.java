package controleur;

import com.chat.client.ClientChat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurTicTacToe implements ActionListener {

    private ClientChat clientChat;

    public EcouteurTicTacToe(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //à compléter
    }
}
