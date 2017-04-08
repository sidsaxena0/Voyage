package com.sid.voyage.pojo;

/**
 * Created by sid on 08/04/17.
 */

public class Suggestion {

    private String name, country, price, currency, cityId, image;

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }


    public String getPrice() {
        return price;
    }


}
