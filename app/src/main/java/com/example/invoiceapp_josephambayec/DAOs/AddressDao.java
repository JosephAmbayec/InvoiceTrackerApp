package com.example.invoiceapp_josephambayec.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.generic.GenericDao;

import java.util.List;

@Dao
public abstract class AddressDao extends GenericDao<Address> {
    public AddressDao() {
        super(TABLE_NAME);
    }
    private static final String TABLE_NAME = "address_table";

    public AddressDao(String TABLE_NAME) {
        super(TABLE_NAME);
    }

    @Query("SELECT * from " + TABLE_NAME + " ORDER BY id ASC")
    public abstract LiveData<List<Address>> getAll();

    @Query("SELECT * from " + TABLE_NAME +" LIMIT 1")
    public abstract Address[] getAnyAddress();

    @Query("SELECT * from " + TABLE_NAME + " WHERE id = :id")
    public abstract Address getAddressById(int id);

    @Query("SELECT * from " + TABLE_NAME + " WHERE customerId = :customerId ORDER BY id")
    public abstract List<Address> getAddressesByCustomerId(int customerId);
}
