
package com.example.invoiceapp_josephambayec.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_josephambayec.entities.Invoice;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;
import com.example.invoiceapp_josephambayec.repositories.InvoiceRepository;

import java.util.List;


public class InvoiceViewModel extends AndroidViewModel {

    private InvoiceRepository mRepository;

    private LiveData<List<Invoice>> mAllInvoices;

    public InvoiceViewModel(Application application) {
        super(application);
        mRepository = new InvoiceRepository(application);
        mAllInvoices = mRepository.getAllInvoices();
    }

    public LiveData<List<Invoice>> getAllInvoices() {

        return mAllInvoices;
    }

    public long insert(Invoice invoice) {
        return mRepository.insert(invoice);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteInvoice(Invoice invoice) {
        mRepository.deleteInvoice(invoice);
    }

    public void update(Invoice invoice) {
        mRepository.update(invoice);
    }

    public Invoice getInvoiceById(int id){
        return mRepository.getInvoiceById(id);
    }

    public LiveData<List<Invoice>>  getInvoicesForCustomer(int customerId) { return mRepository.getInvoicesForCustomer(customerId); }

    public void updateInvoiceTotal(List<InvoiceDetail> invoiceDetailList, int invoiceId) {
        mRepository.updateInvoiceTotal(invoiceDetailList, invoiceId);
    }
}
