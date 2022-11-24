
package com.example.invoiceapp_josephambayec.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;
import com.example.invoiceapp_josephambayec.repositories.InvoiceDetailRepository;

import java.util.List;


public class InvoiceDetailViewModel extends AndroidViewModel {

    private InvoiceDetailRepository mRepository;

    private LiveData<List<InvoiceDetail>> mAllInvoiceDetails;

    public InvoiceDetailViewModel(Application application) {
        super(application);
        mRepository = new InvoiceDetailRepository(application);
        mAllInvoiceDetails = mRepository.getAllInvoiceDetails();
    }

    public LiveData<List<InvoiceDetail>> getAllInvoiceDetails() {

        return mAllInvoiceDetails;
    }

    public long insert(InvoiceDetail invoiceDetail) {
        return mRepository.insert(invoiceDetail);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteInvoiceDetail(InvoiceDetail invoiceDetail) {
        mRepository.deleteInvoice(invoiceDetail);
    }

    public void update(InvoiceDetail invoiceDetail) {
        mRepository.update(invoiceDetail);
    }

    public InvoiceDetail getInvoiceById(int id){
        return mRepository.getInvoiceDetailById(id);
    }

    public LiveData<List<InvoiceDetail>>  getInvoiceDetailsForInvoice(int invoiceId) { return mRepository.getInvoiceDetailsForInvoice(invoiceId); }
}
