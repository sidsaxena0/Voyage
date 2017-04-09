package com.sid.voyage.models;

/**
 * Created by sid on 09/04/17.
 */

public class Trip {

    private String id,date,origin,destination,by,userId,interests,image;

    public void setId(String id) {
        this.id = id;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getBy() {
        return by;
    }

    public String getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    public String getInterests() {
        return interests;
    }

    public String getImage() {
        return image;
    }

    public String getOrigin() {
        return origin;
    }

    public String getUserId() {
        return userId;
    }
}
