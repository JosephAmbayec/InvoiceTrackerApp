package com.example.invoiceapp_josephambayec.DAOs;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;
import com.example.invoiceapp_josephambayec.generic.GenericDao;

import java.util.List;

@Dao
public abstract class InvoiceDetailDao extends GenericDao<InvoiceDetail> {
    private static final String TABLE_NAME = "invoiceDetail_table";
    public InvoiceDetailDao() {
        super(TABLE_NAME);
    }


    public InvoiceDetailDao(String TABLE_NAME) {
        super(TABLE_NAME);
    }

    @Query("SELECT * from " + TABLE_NAME)
    public abstract LiveData<List<InvoiceDetail>> getAll();

    @Query("SELECT * from " + TABLE_NAME + " WHERE invoiceId = :invoiceId")
    public abstract LiveData<List<InvoiceDetail>> getInvoiceDetailsByInvoice(int invoiceId);

    @Query("SELECT * from " + TABLE_NAME + " WHERE id = :id")
    public abstract InvoiceDetail getById(int id);
}