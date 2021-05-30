package ru.sfedu.HospitalityNetwork.models;

import org.hibernate.validator.constraints.Length;

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

    @Length(max = 256, message = "Максимальная длина 256 символов")
    private String causeVisit;
    @Length(max = 256, message = "Максимальная длина 256 символов")
    private String aboutBaggage;

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
        return super.getAuthor().getFullName();
    }

    public Long getAuthorId() {
        return super.getAuthor().getId();
    }
}
