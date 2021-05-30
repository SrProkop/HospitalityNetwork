package ru.sfedu.HospitalityNetwork.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@MappedSuperclass
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @Length(max = 256, message = "Максимальная длина 256 символов")
    private String name;
    @Length(max = 1028, message = "Максимальная длина 1028 символов")
    private String aboutOffer;
    @Length(max = 64, message = "Максимальная длина 64 символа")
    private String country;
    @Length(max = 64, message = "Максимальная длина 64 символа")
    private String city;

    private int views;

    private boolean typeOffer;

    public Offer(String name, String aboutOffer, String country, String city, User author) {
        this.name = name;
        this.aboutOffer = aboutOffer;
        this.country = country;
        this.city = city;
    }

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutOffer() {
        return aboutOffer;
    }

    public void setAboutOffer(String aboutOffer) {
        this.aboutOffer = aboutOffer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isTypeOffer() {
        return typeOffer;
    }

    public void setTypeOffer(boolean typeOffer) {
        this.typeOffer = typeOffer;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }


}
