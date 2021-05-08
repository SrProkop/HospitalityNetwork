package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "OfferGuest")
public class OfferGuest extends Offer{

    public OfferGuest(String name, String country, String city, String aboutOffer, User author, String causeVisit, String aboutBaggage) {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setAboutOffer(aboutOffer);
        this.setTypeOffer(true);
        this.setAuthor(author);
        this.setCauseVisit(causeVisit);
        this.setAboutBaggage(aboutBaggage);
    }

    public OfferGuest() {

    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String causeVisit;
    private int numberDay;
    private String aboutBaggage;

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

    public String getAboutBaggage() {
        return aboutBaggage;
    }

    public void setAboutBaggage(String aboutBaggage) {
        this.aboutBaggage = aboutBaggage;
    }

    public String getCauseVisit() {
        return causeVisit;
    }

    public void setCauseVisit(String causeVisit) {
        this.causeVisit = causeVisit;
    }

    public String getAuthorName() {
        return author.getFullName();
    }

    public Long getAuthorId() {
        return author.getId();
    }
}
