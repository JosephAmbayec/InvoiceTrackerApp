
package com.example.invoiceapp_josephambayec.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.repositories.AddressRepository;

import java.util.List;


public class AddressViewModel extends AndroidViewModel {

    private AddressRepository mRepository;

    private LiveData<List<Address>> mAllAddresss;

    public AddressViewModel(Application application) {
        super(application);
        mRepository = new AddressRepository(application);
        mAllAddresss = mRepository.getAllAddresses();
    }

    LiveData<List<Address>> getAllAddress() {
        return mAllAddresss;
    }

    public void insert(Address address) {
        mRepository.insert(address);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAddress(Address address) {
        mRepository.deleteAddress(address);
    }

    public void update(Address address) {
        mRepository.update(address);
    }

    public Address getAddressById(int id){
        return mRepository.getAddressById(id);
    }

    public List<Address> getAddressesByCustomerId(int customerId) { return mRepository.getAddressesByCustomerId(customerId); }
}
