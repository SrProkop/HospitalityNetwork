package ru.sfedu.HospitalityNetwork.models;

import javax.persistence.*;

@MappedSuperclass
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String aboutOffer;

    private String country;

    private String city;

    private int views;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private boolean typeOffer;

    public Offer(String name, String aboutOffer, String country, String city, User author) {
        this.name = name;
        this.aboutOffer = aboutOffer;
        this.country = country;
        this.city = city;
        this.author = author;
    }

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public Long getAuthorId() {
        return author.getId();
    }

}
