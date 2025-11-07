package com.chat.serveur;

public class SalonPrive {

    private String host, guest;

    public SalonPrive(String host, String guest) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;
        SalonPrive other = (SalonPrive) obj;

        return (host.equals(other.getHost()) && guest.equals(other.getGuest())
        || host.equals(other.getGuest()) && guest.equals(other.getHost()));
    }
}
