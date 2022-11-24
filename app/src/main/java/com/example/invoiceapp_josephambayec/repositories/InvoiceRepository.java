package com.example.invoiceapp_josephambayec.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.invoiceapp_josephambayec.DAOs.InvoiceDao;
import com.example.invoiceapp_josephambayec.CustomerRoomDatabase;
import com.example.invoiceapp_josephambayec.DAOs.InvoiceDetailDao;
import com.example.invoiceapp_josephambayec.entities.Invoice;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.util.List;

public class InvoiceRepository {
    private InvoiceDao mInvoiceDao;
    private InvoiceDetailDao mInvoiceDetailDao;
    private LiveData<List<Invoice>> mAllInvoices;
    private LiveData<List<InvoiceDetail>> mAllInvoiceDetails;

    public InvoiceRepository(Application application){
        CustomerRoomDatabase db = CustomerRoomDatabase.getDatabase(application);
        mInvoiceDao = db.invoiceDao();
        mAllInvoices = mInvoiceDao.getAll();
        mInvoiceDetailDao = db.invoiceDetailDao();
        mAllInvoiceDetails = mInvoiceDetailDao.getAll();
    }

    public LiveData<List<Invoice>> getAllInvoices() { return mAllInvoices; }

    public Long insert(Invoice invoice) {
        try {
            return new insertAsyncTask(mInvoiceDao).execute(invoice).get();
        }
        catch (Exception exception){
            return null;
        }

    }

    public void update(Invoice invoice)  {
        new updateInvoiceAsyncTask(mInvoiceDao, mInvoiceDetailDao).execute(invoice);
    }

    public void deleteAll()  {
        new deleteAllInvoicesAsyncTask(mInvoiceDao).execute();
    }

    // Must run off main thread
    public void deleteInvoice(Invoice invoice) {
        new deleteInvoiceAsyncTask(mInvoiceDao).execute(invoice);
    }

    public Invoice getInvoiceById(int id) {
        try{
            return new getInvoiceByIdAsyncTask(mInvoiceDao).execute(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

    public LiveData<List<Invoice>> getInvoicesForCustomer(int customerId) {
        try {
            LiveData<List<Invoice>>  invoiceList = new InvoiceRepository.getInvoicesForCustomerAsyncTask(mInvoiceDao).execute(customerId).get();
            return invoiceList;
        }
        catch (Exception exception){
            return null;
        }
    }

    public void updateInvoiceTotal(List<InvoiceDetail> invoiceDetailList, int invoiceId) {
        new InvoiceRepository.updateInvoiceTotalAsyncTask(mInvoiceDao).execute(invoiceDetailList, invoiceId);
    }

    private static class updateInvoiceTotalAsyncTask extends AsyncTask<Object, Void, Void>{
        private InvoiceDao mAsyncTaskDao;

        updateInvoiceTotalAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Object... invoiceDetails) {
            if (((List<InvoiceDetail>) invoiceDetails[0]).size() != 0){
                double total = 0;
                Invoice invoice = ((Invoice) mAsyncTaskDao.getById((int)invoiceDetails[1]));
                for (InvoiceDetail invoiceDetail : (List<InvoiceDetail>)invoiceDetails[0]){
                    total += invoiceDetail.getLineTotal();
                }
                invoice.setTotal(total);
                mAsyncTaskDao.update(invoice);
            }
            else {
                Invoice invoice = ((Invoice) mAsyncTaskDao.getById((int)invoiceDetails[1]));
                invoice.setTotal(0);
                mAsyncTaskDao.update(invoice);
            }
            return null;
        }
    }

    private static class getInvoicesForCustomerAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Invoice>> >{
        private InvoiceDao mAsyncTaskDao;

        getInvoicesForCustomerAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Invoice>> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getInvoicesForCustomer(integers[0]);
        }
    }

    private static class getInvoiceByIdAsyncTask extends  AsyncTask<Integer, Void, Invoice>{
        private InvoiceDao mAsyncTaskDao;

        getInvoiceByIdAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Invoice doInBackground(Integer... integers) {
            return mAsyncTaskDao.getById(integers[0]);
        }
    }

    private static class insertAsyncTask extends AsyncTask<Invoice, Void, Long> {

        private InvoiceDao mAsyncTaskDao;

        insertAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Invoice... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    /**
     * Deletes all Invoices from the database (does not delete the table).
     */
    private static class deleteAllInvoicesAsyncTask extends AsyncTask<Void, Void, Void> {
        private InvoiceDao mAsyncTaskDao;

        deleteAllInvoicesAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single Invoice from the database.
     */
    private static class deleteInvoiceAsyncTask extends AsyncTask<Invoice, Void, Void> {
        private InvoiceDao mAsyncTaskDao;

        deleteInvoiceAsyncTask(InvoiceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Invoice... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a Invoice in the database.
     */
    private static class updateInvoiceAsyncTask extends AsyncTask<Invoice, Void, Void> {
        private InvoiceDao mAsyncTaskDao;
        private InvoiceDetailDao mAsyncTaskDaoDetail;

        updateInvoiceAsyncTask(InvoiceDao dao, InvoiceDetailDao daoDetail) {
            mAsyncTaskDao = dao;
            mAsyncTaskDaoDetail = daoDetail;
        }

        @Override
        protected Void doInBackground(final Invoice... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
