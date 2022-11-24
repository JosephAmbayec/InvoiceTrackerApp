package com.example.invoiceapp_josephambayec.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entity class that represents a customer in the database
 */
@Entity(tableName = "customer_table")
public class Customer {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;


  //  @ForeignKey(entity = Address.class, parentColumns = {"id"}, childColumns = {"defaultAddressId"}, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "defaultAddressId")
    private int defaultAddressId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getDefaultAddressId() {
        return defaultAddressId;
    }

    public void setDefaultAddressId(Integer defaultAddressId) {
        if (defaultAddressId == null){
            this.defaultAddressId = -1;
            return;
        }
        this.defaultAddressId = defaultAddressId;
    }


    public Customer(@NonNull String name, Integer defaultAddressId){
        this.name = name;
        this.setDefaultAddressId(defaultAddressId);
    }

    @Ignore
    public Customer(int id, @NonNull String name, Integer defaultAddressId){
        this.id = id;
        this.name = name;
        this.defaultAddressId = defaultAddressId;
    }
}
