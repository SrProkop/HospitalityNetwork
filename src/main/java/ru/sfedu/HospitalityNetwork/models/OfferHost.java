package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private boolean personalMeeting;
    private String addressHouse;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isPersonalMeeting() {
        return personalMeeting;
    }

    public void setPersonalMeeting(boolean personalMeeting) {
        this.personalMeeting = personalMeeting;
    }

    public String getAddressHouse() {
        return addressHouse;
    }

    public void setAddressHouse(String addressHouse) {
        this.addressHouse = addressHouse;
    }

    public String getAuthorName() {
        return author.getFullName();
    }

    public Long getAuthorId() {
        return author.getId();
    }
}
