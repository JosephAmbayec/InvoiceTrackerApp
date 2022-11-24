package com.example.invoiceapp_josephambayec.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.invoiceapp_josephambayec.DAOs.AddressDao;
import com.example.invoiceapp_josephambayec.CustomerRoomDatabase;
import com.example.invoiceapp_josephambayec.entities.Address;

import java.util.List;

public class AddressRepository {
    private AddressDao mAddressDao;
    private LiveData<List<Address>> mAllAddresses;

    public AddressRepository(Application application){
        CustomerRoomDatabase db = CustomerRoomDatabase.getDatabase(application);
        mAddressDao = db.addressDao();
        mAllAddresses = mAddressDao.getAll();
    }

    public LiveData<List<Address>> getAllAddresses() { return mAllAddresses; }

    public void insert(Address address) {
        new insertAsyncTask(mAddressDao).execute(address);
    }

    public void update(Address address)  {
        new updateAddressAsyncTask(mAddressDao).execute(address);
    }

    public void deleteAll()  {
        new deleteAllAddressAsyncTask(mAddressDao).execute();
    }

    // Must run off main thread
    public void deleteAddress(Address address) {
        new deleteAddressAsyncTask(mAddressDao).execute(address);
    }

    public Address getAddressById(int id){
        try{
            Address address = new getAddressByIdAsyncTask(mAddressDao).execute(id).get();
            return address;
        }
        catch (Exception exception) {
            return null;
        }
    }
    public List<Address> getAddressesByCustomerId(int customerId){
        try {
            List<Address> addressList = new getAddressesByCustomerIdAsyncTask(mAddressDao).execute(customerId).get();
            return addressList;
        }
        catch (Exception exception){
            return null;
        }
    }

    private static class getAddressesByCustomerIdAsyncTask extends  AsyncTask<Integer, Void, List<Address>>{
        private AddressDao mAsyncTaskDao;

        getAddressesByCustomerIdAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<Address> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getAddressesByCustomerId(integers[0]);
        }
    }

    private static class getAddressByIdAsyncTask extends AsyncTask<Integer, Void, Address>{
        private AddressDao mAsyncTaskDao;

        getAddressByIdAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Address doInBackground(Integer... integers) {
            Address address = mAsyncTaskDao.getAddressById(integers[0]);
            return address;
        }
    }


    private static class insertAsyncTask extends AsyncTask<Address, Void, Void> {

        private AddressDao mAsyncTaskDao;

        insertAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all addresss from the database (does not delete the table).
     */
    private static class deleteAllAddressAsyncTask extends AsyncTask<Void, Void, Void> {
        private AddressDao mAsyncTaskDao;

        deleteAllAddressAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single address from the database.
     */
    private static class deleteAddressAsyncTask extends AsyncTask<Address, Void, Void> {
        private AddressDao mAsyncTaskDao;

        deleteAddressAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    /**
     *  Updates a address in the database.
     */
    private static class updateAddressAsyncTask extends AsyncTask<Address, Void, Void> {
        private AddressDao mAsyncTaskDao;

        updateAddressAsyncTask(AddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Address... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
