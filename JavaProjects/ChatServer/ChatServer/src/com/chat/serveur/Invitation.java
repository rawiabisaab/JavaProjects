package com.chat.serveur;

public class Invitation {

    private String host, guest;

    public  Invitation(String host, String guest) {
        setHost(host);
        setGuest(guest);
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getGuest() {
        return guest;
    }
    public void setGuest(String guest) {
        this.guest = guest;
    }

}
