package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.Entity;

@Entity(name = "OfferHost")
public class OfferHost extends Offer{

    public OfferHost(String name, String country, String city, String aboutOffer, User author) {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setAboutOffer(aboutOffer);
        this.setTypeOffer(true);
        this.setAuthor(author);
    }

    public OfferHost() {

    }

    private boolean personalMeeting;
    private String addressHouse;

}
