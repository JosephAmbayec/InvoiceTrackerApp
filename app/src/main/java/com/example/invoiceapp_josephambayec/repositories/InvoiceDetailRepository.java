package com.example.invoiceapp_josephambayec.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.invoiceapp_josephambayec.CustomerRoomDatabase;
import com.example.invoiceapp_josephambayec.DAOs.InvoiceDetailDao;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.util.List;

public class InvoiceDetailRepository {
    private InvoiceDetailDao mInvoiceDetailDao;
    private LiveData<List<InvoiceDetail>> mAllInvoiceDetails;

    public InvoiceDetailRepository(Application application){
        CustomerRoomDatabase db = CustomerRoomDatabase.getDatabase(application);
        mInvoiceDetailDao = db.invoiceDetailDao();
        mAllInvoiceDetails = mInvoiceDetailDao.getAll();
    }

    public LiveData<List<InvoiceDetail>> getAllInvoiceDetails() { return mAllInvoiceDetails; }

    public Long insert(InvoiceDetail invoice) {
        try {
            return new insertAsyncTask(mInvoiceDetailDao).execute(invoice).get();
        }
        catch (Exception exception){
            return null;
        }

    }

    public void update(InvoiceDetail invoice)  {
        new updateInvoiceAsyncTask(mInvoiceDetailDao).execute(invoice);
    }

    public void deleteAll()  {
        new deleteAllInvoicesAsyncTask(mInvoiceDetailDao).execute();
    }

    public void deleteInvoice(InvoiceDetail invoice) {
        new deleteInvoiceAsyncTask(mInvoiceDetailDao).execute(invoice);
    }

    public InvoiceDetail getInvoiceDetailById(int id){
        try{
            return new InvoiceDetailRepository.getInvoiceDetailByIdAsyncTask(mInvoiceDetailDao).execute(id).get();
        }
        catch (Exception e){
            return null;
        }
    }



    private static class insertAsyncTask extends AsyncTask<InvoiceDetail, Void, Long> {

        private InvoiceDetailDao mAsyncTaskDao;

        insertAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final InvoiceDetail... params) {
            InvoiceDetail invoiceDetail = params[0];
            invoiceDetail.setLineTotal(invoiceDetail.getQuantity() * invoiceDetail.getPricePerUnit());
            return mAsyncTaskDao.insert(invoiceDetail);
        }
    }
    private static class getInvoiceDetailByIdAsyncTask extends  AsyncTask<Integer, Void, InvoiceDetail>{
        private InvoiceDetailDao mAsyncTaskDao;

        getInvoiceDetailByIdAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected InvoiceDetail doInBackground(Integer... integers) {
            return mAsyncTaskDao.getById(integers[0]);
        }
    }

    public LiveData<List<InvoiceDetail>> getInvoiceDetailsForInvoice(int invoiceId) {
        try {
            LiveData<List<InvoiceDetail>>  invoiceDetailList = new InvoiceDetailRepository.getInvoiceDetailsForInvoiceAsyncTask(mInvoiceDetailDao).execute(invoiceId).get();
            return invoiceDetailList;
        }
        catch (Exception exception){
            return null;
        }
    }

    private static class getInvoiceDetailsForInvoiceAsyncTask extends AsyncTask<Integer, Void, LiveData<List<InvoiceDetail>> >{
        private InvoiceDetailDao mAsyncTaskDao;

        getInvoiceDetailsForInvoiceAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<InvoiceDetail>> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getInvoiceDetailsByInvoice(integers[0]);
        }
    }

        /**
     * Deletes all Invoices from the database (does not delete the table).
     */
    private static class deleteAllInvoicesAsyncTask extends AsyncTask<Void, Void, Void> {
        private InvoiceDetailDao mAsyncTaskDao;

        deleteAllInvoicesAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single InvoiceDetail from the database.
     */
    private static class deleteInvoiceAsyncTask extends AsyncTask<InvoiceDetail, Void, Void> {
        private InvoiceDetailDao mAsyncTaskDao;

        deleteInvoiceAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InvoiceDetail... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a InvoiceDetail in the database.
     */
    private static class updateInvoiceAsyncTask extends AsyncTask<InvoiceDetail, Void, Void> {
        private InvoiceDetailDao mAsyncTaskDao;

        updateInvoiceAsyncTask(InvoiceDetailDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InvoiceDetail... params) {
            params[0].setLineTotal(params[0].getQuantity() * params[0].getPricePerUnit());
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
