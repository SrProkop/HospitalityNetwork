package ru.sfedu.HospitalityNetwork.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "OfferHost")
public class OfferHost extends Offer{

    public OfferHost(String name, String country, String city, String aboutOffer, User author, String addressHouse, String aboutHouse) {
        this.setName(name);
        this.setCountry(country);
        this.setCity(city);
        this.setAboutOffer(aboutOffer);
        this.setTypeOffer(true);
        this.setAuthor(author);
        this.setAddressHouse(addressHouse);
        this.setAboutHouse(aboutHouse);
    }

    public OfferHost() {

    }
    @Length(max = 128, message = "Максимальная длина 128 символов")
    private String addressHouse;
    @Length(max = 256, message = "Максимальная длина 256 символов")
    private String aboutHouse;
    private String avatar;


    public String getAddressHouse() {
        return addressHouse;
    }

    public void setAddressHouse(String addressHouse) {
        this.addressHouse = addressHouse;
    }

    public String getAboutHouse() {
        return aboutHouse;
    }

    public void setAboutHouse(String aboutHouse) {
        this.aboutHouse = aboutHouse;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAuthorName() {
        return super.getAuthor().getFullName();
    }

    public Long getAuthorId() {
        return super.getAuthor().getId();
    }
}
