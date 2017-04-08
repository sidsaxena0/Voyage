package com.sid.voyage.models;

/**
 * Created by sid on 08/04/17.
 */

public class Hotel {

    private String address,name,city,country,description,cityId,image,latitude,longitude,minimumPrice,state,id,xid;



    public void setState(String state) {
        this.state = state;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public void setMinimumPrice(String minimumPrice) {
        this.minimumPrice = minimumPrice;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getCityId() {
        return cityId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }


    public String getXid() {
        return xid;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMinimumPrice() {
        return minimumPrice;
    }
}

