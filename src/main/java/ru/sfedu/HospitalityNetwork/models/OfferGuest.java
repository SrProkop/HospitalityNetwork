package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private int numberDay;
    private int weightBaggage;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }

    public int getWeightBaggage() {
        return weightBaggage;
    }

    public void setWeightBaggage(int weightBaggage) {
        this.weightBaggage = weightBaggage;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public Long getAuthorId() {
        return author.getId();
    }
}
