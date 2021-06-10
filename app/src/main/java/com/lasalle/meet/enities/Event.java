package com.lasalle.meet.enities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    @Expose(serialize = true, deserialize = true)
    private int ID;
    @Expose(serialize = true, deserialize = true)
    private String name;
    @Expose(serialize = true, deserialize = true)
    private int owner_id;
    @Expose(serialize = true, deserialize = true)
    private Date date;
    @Expose(serialize = true, deserialize = true)
    private String image;
    @Expose(serialize = true, deserialize = true)
    private String location;
    @Expose(serialize = true, deserialize = true)
    private String description;
    @Expose(serialize = true, deserialize = true)
    private Date eventStart_date;
    @Expose(serialize = true, deserialize = true)
    private Date eventEnd_date;
    @Expose(serialize = true, deserialize = true)
    private int n_participators;
    @Expose(serialize = false, deserialize = false)
    private String type;
    @Expose(serialize = false, deserialize = false)
    private String comentary;
    @Expose(serialize = false, deserialize = false)
    private int punctuation;

    public Event(int ID, String name, int owner_id, Date date, String image, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String comentary, int punctuation) {
        this.ID = ID;
        this.name = name;
        this.owner_id = owner_id;
        this.date = date;
        this.image = image;
        this.location = location;
        this.description = description;
        this.eventStart_date = eventStart_date;
        this.eventEnd_date = eventEnd_date;
        this.n_participators = n_participators;
        this.type = type;
        this.comentary = comentary;
        this.punctuation = punctuation;
    }
}
