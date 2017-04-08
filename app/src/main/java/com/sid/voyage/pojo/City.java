package com.sid.voyage.pojo;

/**
 * Created by sid on 08/04/17.
 */

public class City {

    private String name,cityId,country,state;
    private Double lat,aLong;

    public void setName(String name) {
        this.name = name;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setaLong(Double aLong) {
        this.aLong = aLong;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public String getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public Double getaLong() {
        return aLong;
    }

    public Double getLat() {
        return lat;
    }


    public String getState() {
        return state;
    }
}
