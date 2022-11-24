
package com.example.invoiceapp_josephambayec.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_josephambayec.entities.Customer;
import com.example.invoiceapp_josephambayec.repositories.CustomerRepository;
import java.util.List;


public class CustomerViewModel extends AndroidViewModel {

    private CustomerRepository mRepository;

    private LiveData<List<Customer>> mAllCustomers;

    public CustomerViewModel(Application application) {
        super(application);
        mRepository = new CustomerRepository(application);
        mAllCustomers = mRepository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {

        return mAllCustomers;
    }

    public long insert(Customer customer) {
        return mRepository.insert(customer);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteCustomer(Customer customer) {
        mRepository.deleteCustomer(customer);
    }

    public void update(Customer customer) {
        mRepository.update(customer);
    }

    public int customerGetDefaultAddress(Customer customer){
        return mRepository.getDefaultAddress(customer);
    }

    public Customer getCustomerById(int id){
        return mRepository.getCustomerById(id);
    }
}
