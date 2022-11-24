package com.example.invoiceapp_josephambayec.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.invoiceapp_josephambayec.entities.Customer;
import com.example.invoiceapp_josephambayec.generic.GenericDao;

import java.util.List;

@Dao
public abstract class CustomerDao extends GenericDao<Customer> {
    public CustomerDao() {
        super(TABLE_NAME);
    }
    private static final String TABLE_NAME = "customer_table";
    public CustomerDao(String TABLE_NAME) {
        super(TABLE_NAME);
    }
    @Query("SELECT * from " + TABLE_NAME + " ORDER BY name ASC")
    public abstract LiveData<List<Customer>> getAll();
    @Query("SELECT * from " + TABLE_NAME +" LIMIT 1")
    public abstract Customer[] getAnyCustomer();
    @Query("SELECT defaultAddressId from " + TABLE_NAME + " WHERE id = :id")
    public abstract int getDefaultAddressId(int id);
    @Query("SELECT * from " + TABLE_NAME + " WHERE id = :id")
    public abstract Customer getCustomerById(int id);
}
