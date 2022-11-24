package com.example.invoiceapp_josephambayec.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity class that represents an address in the database
 */
@Entity(tableName = "address_table", foreignKeys = {@ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customerId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Address  {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int customerId;

    private String street;

    private String city;

    private String province;

    private String country;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Ignore
    public Address(int customerId, String street, String city, String province, String country){
        this.id = id;
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
    }


    public Address(int id, int customerId, String street, String city, String province, String country){
        this.id = id;
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
    }

    @Override
    public String toString(){
        return this.getStreet() + ", " + this.getCity() + ", " + this.getProvince() + ", " + this.getCountry();
    }

    public static Address fromStringToAddress(int customerId, String stringAddress){
        String strings[] = stringAddress.split(",");

        String street = strings[0];
        String city = strings[1];
        String province = strings[2];
        String country = strings[3];
        return new Address(customerId, street, city, province, country);
    }
}
