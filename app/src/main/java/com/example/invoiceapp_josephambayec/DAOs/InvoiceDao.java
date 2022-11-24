package com.example.invoiceapp_josephambayec.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.invoiceapp_josephambayec.entities.Invoice;
import com.example.invoiceapp_josephambayec.generic.GenericDao;

import java.util.List;

@Dao
public abstract class InvoiceDao extends GenericDao<Invoice> {
    private static final String TABLE_NAME = "invoice_table";
    public InvoiceDao() {
        super(TABLE_NAME);
    }

    @Query("SELECT * from " + TABLE_NAME)
    public abstract LiveData<List<Invoice>> getAll();

    @Query("SELECT * from " + TABLE_NAME + " WHERE customerId = :customerId")
    public abstract LiveData<List<Invoice>> getInvoicesForCustomer(int customerId);

    @Query("SELECT * from " + TABLE_NAME + " WHERE id = :id")
    public abstract Invoice getById(int id);
}
