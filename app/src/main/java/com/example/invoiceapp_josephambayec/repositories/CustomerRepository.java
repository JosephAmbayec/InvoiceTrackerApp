package com.example.invoiceapp_josephambayec.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.invoiceapp_josephambayec.DAOs.CustomerDao;
import com.example.invoiceapp_josephambayec.CustomerRoomDatabase;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.List;

public class CustomerRepository {
    private CustomerDao mCustomerDao;
    private LiveData<List<Customer>> mAllCustomers;

    public CustomerRepository(Application application){
        CustomerRoomDatabase db = CustomerRoomDatabase.getDatabase(application);
        mCustomerDao = db.customerDao();
        mAllCustomers = mCustomerDao.getAll();
    }

    public LiveData<List<Customer>> getAllCustomers() { return mAllCustomers; }

    public Long insert(Customer customer) {
        try {
            return new insertAsyncTask(mCustomerDao).execute(customer).get();
        }
        catch (Exception exception){
            return null;
        }

    }

    public void update(Customer customer)  {
        new updateCustomerAsyncTask(mCustomerDao).execute(customer);
    }

    public void deleteAll()  {
        new deleteAllCustomersAsyncTask(mCustomerDao).execute();
    }

    // Must run off main thread
    public void deleteCustomer(Customer customer) {
        new deleteCustomerAsyncTask(mCustomerDao).execute(customer);
    }

    public int getDefaultAddress(Customer customer){
        try{
            return new getDefaultAddressIdAsyncTask(mCustomerDao).execute(customer).get();
        }
        catch (Exception e){
            return -1;
        }
    }

    public Customer getCustomerById(int id) {
        try{
            return new getCustomerByIdAsyncTask(mCustomerDao).execute(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

    private static class getCustomerByIdAsyncTask extends  AsyncTask<Integer, Void, Customer>{
        private CustomerDao mAsyncTaskDao;

        getCustomerByIdAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Customer doInBackground(Integer... integers) {
            return mAsyncTaskDao.getCustomerById(integers[0]);
        }
    }

    private static class getDefaultAddressIdAsyncTask extends AsyncTask<Customer, Void, Integer>{
        private CustomerDao mAsyncTaskDao;

        getDefaultAddressIdAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Integer doInBackground(Customer... customers) {
            return mAsyncTaskDao.getDefaultAddressId(customers[0].getId());
        }
    }

    private static class insertAsyncTask extends AsyncTask<Customer, Void, Long> {

        private CustomerDao mAsyncTaskDao;

        insertAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Customer... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    /**
     * Deletes all customers from the database (does not delete the table).
     */
    private static class deleteAllCustomersAsyncTask extends AsyncTask<Void, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        deleteAllCustomersAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single customer from the database.
     */
    private static class deleteCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        deleteCustomerAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Customer... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a customer in the database.
     */
    private static class updateCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao mAsyncTaskDao;

        updateCustomerAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Customer... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
