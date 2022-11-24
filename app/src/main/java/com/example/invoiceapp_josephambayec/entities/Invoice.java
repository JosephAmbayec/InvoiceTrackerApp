package com.example.invoiceapp_josephambayec.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity (tableName = "invoice_table", foreignKeys = {@ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customerId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Invoice {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int customerId;

    @NonNull
    private String deliveryAddress;

    @NonNull
    private double total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Invoice(int id, int customerId, String deliveryAddress, double total){
        this.id = id;
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.total = total;
    }

    @Ignore
    public Invoice(int customerId, String deliveryAddress, double total){
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.total = total;
    }
}
