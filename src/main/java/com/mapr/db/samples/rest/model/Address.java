package com.mapr.db.samples.rest.model;

public class Address {

    private String type;
    private String line;
    private String town;
    private String postcode;
    private String country;

    public Address() {
    }

    public Address(String type, String line, String line2, String town, String postcode, String country) {
        this.type = type;
        this.line = line;
        this.town = town;
        this.postcode = postcode;
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "type='" + type + '\'' +
                ", line='" + line + '\'' +
                ", town='" + town + '\'' +
                ", postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
