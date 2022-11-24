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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invoiceapp_josephambayec.ListAdapters.InvoiceDetailListAdapter;
import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceViewModel;
import com.example.invoiceapp_josephambayec.entities.Invoice;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceDetailViewModel;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowInvoiceActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.invoiceapp_josephambayec.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_INVOICE_ID = "extra_data_invoice_id";
    public static final String EXTRA_DATA_INVOICE_DETAIL_ID = "extra_data_invoice_detail_id";
    public static final String EXTRA_DATA_ADDRESS = "extra_data_address";
    public static final String EXTRA_REPLY_INVOICE_DETAIL_ID = "extra_reply_invoice_id";
    public static final String EXTRA_REPLY_PRODUCT_NAME = "extra_reply_product_name";
    public static final String EXTRA_REPLY_PRICE = "extra_data_price";
    public static final String EXTRA_REPLY_QUANTITY = "extra_data_quantity";

    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;
    public static final int UPDATE_INVOICE_ACTIVITY_REQUEST_CODE = 8;
    public static final int UPDATE_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE = 9;
    public static final int NEW_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE = 10;

    private InvoiceViewModel mInvoiceViewModel;
    private InvoiceDetailViewModel mInvoiceDetailViewModel;
    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;

    private int invoiceId;

    private TextView addressLabel;
    private TextView totalLabel;
    private Button changeAddressButton;

    private Bundle extras;

    private InvoiceDetailListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice);

        RecyclerView recyclerView = findViewById(R.id.invoiceDetailRecyclerView);
        adapter = new InvoiceDetailListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mInvoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        addressLabel = findViewById(R.id.addressContent_Label);
        totalLabel = findViewById(R.id.invoiceTotalContent_Label);

        changeAddressButton = findViewById(R.id.changeAddress_Button);

        extras = getIntent().getExtras();

        invoiceId = extras.getInt(EXTRA_DATA_INVOICE_ID);
        Invoice invoice = mInvoiceViewModel.getInvoiceById(invoiceId);
        addressLabel.setText(invoice.getDeliveryAddress());
        String total = NumberFormat.getCurrencyInstance().format(invoice.getTotal());
        totalLabel.setText(total);

        // InvoiceDetailViewModel
        mInvoiceDetailViewModel = ViewModelProviders.of(this).get(InvoiceDetailViewModel.class);
        mInvoiceDetailViewModel.getInvoiceDetailsForInvoice(invoice.getId()).observe(this, new Observer<List<InvoiceDetail>>() {
            @Override
            public void onChanged(@Nullable List<InvoiceDetail> invoiceDetails) {
                adapter.setInvoices(invoiceDetails);
                mInvoiceViewModel.updateInvoiceTotal(invoiceDetails, invoiceId);
                Invoice temp = mInvoiceViewModel.getInvoiceById(extras.getInt(EXTRA_DATA_INVOICE_ID));
                String total = NumberFormat.getCurrencyInstance().format(temp.getTotal());
                totalLabel.setText(total);
            }
        });

        changeAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditInvoiceActivity(invoice);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNewInvoiceDetailActivity(invoice);
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
                        InvoiceDetail invoiceDetail = adapter.getInvoiceDetailAtPosition(position);
                        Toast.makeText(ShowInvoiceActivity.this,
                                getString(R.string.delete_customer_preamble) + " Product #" +
                                        invoiceDetail.getId(), Toast.LENGTH_LONG).show();

                        // Delete the invoice detail.
                        mInvoiceDetailViewModel.deleteInvoiceDetail(invoiceDetail);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
        // On Item Click
        adapter.setOnItemClickListener(new InvoiceDetailListAdapter.ClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                InvoiceDetail invoiceDetail = adapter.getInvoiceDetailAtPosition(position);
                launchEditInvoiceDetailActivity(invoiceDetail);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode != UPDATE_INVOICE_ACTIVITY_REQUEST_CODE) {
            String productName = data.getStringExtra(EditInvoiceDetailActivity.EXTRA_REPLY_PRODUCT_NAME);
            int quantity = data.getIntExtra(EditInvoiceDetailActivity.EXTRA_REPLY_QUANTITY, 0);
            double price = data.getDoubleExtra(EditInvoiceDetailActivity.EXTRA_REPLY_PRICE, 0);
            int id = data.getIntExtra(EditInvoiceDetailActivity.EXTRA_REPLY_INVOICE_DETAIL_ID, -1);
            if (requestCode == UPDATE_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE){
                InvoiceDetail invoiceDetail = mInvoiceDetailViewModel.getInvoiceById(id);
                invoiceDetail.setProductName(productName);
                invoiceDetail.setQuantity(quantity);
                invoiceDetail.setPricePerUnit(price);
                invoiceDetail.setLineTotal(price * quantity);
                mInvoiceDetailViewModel.update(invoiceDetail);
            }
            else if (requestCode == NEW_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE){
                InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId, productName, price, quantity);
                invoiceDetail.setInvoiceId(invoiceId);
                mInvoiceDetailViewModel.insert(invoiceDetail);
            }
        }
        else if (requestCode == UPDATE_INVOICE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Invoice invoice = mInvoiceViewModel.getInvoiceById(invoiceId);
            int addressId = data.getIntExtra(EditInvoiceActivity.EXTRA_REPLY_ADDRESS_ID, -1);
            if (addressId != -1) {
                String newAddress = mAddressViewModel.getAddressById(addressId).toString();
                invoice.setDeliveryAddress(newAddress);
                mInvoiceViewModel.update(invoice);
                addressLabel.setText(newAddress);
            }
        }

        else {
            Toast.makeText(this, "Could not update Invoice Detail.", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ((MenuItem) menu.getItem(2)).setTitle("Clear invoice details in all invoices");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            Toast.makeText(this, R.string.clear_invoices_toast_text, Toast.LENGTH_LONG).show();

            // Delete the existing data.
            mInvoiceDetailViewModel.deleteAll();
            // Reset adapter
            adapter.setInvoices(new ArrayList<InvoiceDetail>());
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

    private void launchEditInvoiceDetailActivity(InvoiceDetail invoiceDetail){
        Intent intent = new Intent(ShowInvoiceActivity.this, EditInvoiceDetailActivity.class);
        int id = invoiceDetail.getId();
        intent.putExtra(EXTRA_DATA_ID, id);
        startActivityForResult(intent, UPDATE_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE);
    }

    private void launchNewInvoiceDetailActivity(Invoice invoice){
        Intent intent = new Intent(ShowInvoiceActivity.this, NewInvoiceDetailActivity.class);
        int id = invoice.getId();
        intent.putExtra(EXTRA_DATA_ID, id);
        startActivityForResult(intent, NEW_INVOICE_DETAIL_ACTIVITY_REQUEST_CODE);
    }

    private void launchEditInvoiceActivity(Invoice invoice) {
        Intent intent = new Intent(this, EditInvoiceActivity.class);
        intent.putExtra(EXTRA_DATA_ID, invoice.getCustomerId());
        intent.putExtra(EXTRA_DATA_INVOICE_ID, invoice.getId());
        startActivityForResult(intent, UPDATE_INVOICE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}
