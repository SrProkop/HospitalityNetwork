package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.Entity;

@Entity(name = "OfferGuest")
public class OfferGuest extends Offer{

    public OfferGuest(String name, String country, String city, String aboutOffer, User author) {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setAboutOffer(aboutOffer);
        this.setTypeOffer(true);
        this.setAuthor(author);
    }

    public OfferGuest() {

    }

    private int numberDay;
    private int weightBaggage;

}
