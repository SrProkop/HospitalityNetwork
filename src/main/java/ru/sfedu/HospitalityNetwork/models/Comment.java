package ru.sfedu.HospitalityNetwork.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Comment")
public class Comment {

    public Comment(User userFrom, User userTo, String note){
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.note = note;
    }

    public Comment(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userFrom_id")
    private User userFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userTo_id")
    private User userTo;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Length(max = 255, message = "Вы превысили длину сообщения")
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAuthorName() {
        return userFrom.getFullName();
    }

    public Long getAuthorId() {
        return userFrom.getId();
    }
}
