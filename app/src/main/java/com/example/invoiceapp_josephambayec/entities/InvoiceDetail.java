package com.example.invoiceapp_josephambayec.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity (tableName = "invoiceDetail_table", foreignKeys = {@ForeignKey(entity = Invoice.class, parentColumns = "id", childColumns = "invoiceId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class InvoiceDetail {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int invoiceId;

    @NonNull
    private String productName;

    @NonNull
    private double pricePerUnit;

    @NonNull
    private int quantity;

    @NonNull
    private double lineTotal = this.quantity * this.pricePerUnit;


    public InvoiceDetail (int id, int invoiceId, String productName, double pricePerUnit, int quantity, double lineTotal){
        this.id = id;
        this.invoiceId = invoiceId;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    @Ignore
    public InvoiceDetail (int invoiceId, String productName, double pricePerUnit, int quantity){
        this.invoiceId = invoiceId;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    @NonNull
    public String getProductName() {
        return productName;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
