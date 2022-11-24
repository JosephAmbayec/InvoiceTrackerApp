package com.example.invoiceapp_josephambayec;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.invoiceapp_josephambayec.ListAdapters.InvoiceListAdapter;
import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.entities.Customer;
import com.example.invoiceapp_josephambayec.entities.Invoice;

import java.util.List;

public class ShowInvoicesActivity extends AppCompatActivity {

    public static final int SHOW_INVOICES_ACTIVITY_REQUEST_CODE = 5;
    public static final int NEW_INVOICE_ACTIVITY_REQUEST_CODE = 6;
    public static final int SHOW_INVOICE_ACTIVITY_REQUEST_CODE = 7;
    public static final int UPDATE_INVOICE_ACTIVITY_REQUEST_CODE = 8;

    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_INVOICE_ID = "extra_data_invoice_id";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    private InvoiceViewModel mInvoiceViewModel;
    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;

    InvoiceListAdapter adapter;

    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoices);

        adapter = new InvoiceListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.invoiceRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Bundle extras = getIntent().getExtras();

        customerId = extras.getInt(EXTRA_DATA_ID);

        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        String customerName = mCustomerViewModel.getCustomerById(extras.getInt(EXTRA_DATA_ID)).getName();
        int customerId = extras.getInt(EXTRA_DATA_ID);
        setTitle(customerName + "'s Invoices");

        // InvoiceViewModel
        mInvoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        mInvoiceViewModel.getInvoicesForCustomer(extras.getInt(EXTRA_DATA_ID)).observe(this, new Observer<List<Invoice>>() {
            @Override
            public void onChanged(@Nullable final List<Invoice> invoices) {
                adapter.setInvoices(invoices);
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent = new Intent(ShowInvoicesActivity.this, NewCustomerActivity.class);
                //startActivityForResult(intent, NEW_INVOICE_ACTIVITY_REQUEST_CODE);
                Customer customer = mCustomerViewModel.getCustomerById(customerId);
                int addressId = customer.getDefaultAddressId();
                Address address = mAddressViewModel.getAddressById(addressId);
                Invoice invoice = new Invoice(customerId, "No Address. Enter address in customer view.", 0);
                if (address != null) {
                    invoice.setDeliveryAddress(address.toString());
                }

                int invoiceId = (int) mInvoiceViewModel.insert(invoice);
                adapter.notifyDataSetChanged();
                Toast.makeText(ShowInvoicesActivity.this, "Added Invoice #" + invoiceId, Toast.LENGTH_SHORT).show();
            }
        });


        // Swiping Functionality
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a customer,
                    // delete that customer from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Invoice invoice = adapter.getInvoiceAtPosition(position);
                        Toast.makeText(ShowInvoicesActivity.this,
                                getString(R.string.delete_customer_preamble) + " Invoice #" +
                                        invoice.getId(), Toast.LENGTH_LONG).show();

                        // Delete the invoice.
                        mInvoiceViewModel.deleteInvoice(invoice);

                    }
                });
        helper.attachToRecyclerView(recyclerView);
        // On Item Click
        adapter.setOnItemClickListener(new InvoiceListAdapter.ClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Invoice invoice = adapter.getInvoiceAtPosition(position);
                launchShowInvoiceActivity(invoice);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ((MenuItem) menu.getItem(2)).setTitle("Clear all invoices in all customers");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            Toast.makeText(this, R.string.clear_invoices_toast_text, Toast.LENGTH_LONG).show();

            // Delete the existing data.
            mInvoiceViewModel.deleteAll();
            // Reset adapter
            List<Invoice> invoices = mInvoiceViewModel.getInvoicesForCustomer(customerId).getValue();
            adapter.setInvoices(invoices);
            return true;
        }
        else if (id == R.id.home){
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchShowInvoiceActivity(Invoice invoice) {
        Intent intent = new Intent(this, ShowInvoiceActivity.class);
        intent.putExtra(EXTRA_DATA_ID, invoice.getCustomerId());
        intent.putExtra(EXTRA_DATA_INVOICE_ID, invoice.getId());
        startActivityForResult(intent, SHOW_INVOICE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY_ID, getIntent().getExtras().getInt(EXTRA_DATA_ID));
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}