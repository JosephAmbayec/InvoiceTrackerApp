package com.example.invoiceapp_josephambayec;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceDetailViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;

import java.util.ArrayList;
import java.util.List;

public class NewInvoiceDetailActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.invoiceapp_josephambayec.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_INVOICE_ID = "extra_data_invoice_id";
    public static final String EXTRA_REPLY_INVOICE_DETAIL_ID = "extra_reply_invoice_id";
    public static final String EXTRA_REPLY_PRODUCT_NAME = "extra_reply_product_name";
    public static final String EXTRA_REPLY_PRICE = "extra_data_price";
    public static final String EXTRA_REPLY_QUANTITY = "extra_data_quantity";

    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;


    private InvoiceViewModel mInvoiceViewModel;
    private InvoiceDetailViewModel mInvoiceDetailViewModel;
    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;
    private Bundle extras;
    private List<Address> currentAddressList = new ArrayList<>();

    private EditText productEditView;
    private EditText priceEditView;
    private EditText quantityEditView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice_detail);


        mInvoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        mInvoiceDetailViewModel = ViewModelProviders.of(this).get(InvoiceDetailViewModel.class);
        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        productEditView =  findViewById(R.id.productEditName_View);
        priceEditView =  findViewById(R.id.productEditPrice_View);
        quantityEditView =  findViewById(R.id.productEditQuantity_View);

        extras = getIntent().getExtras();

        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(productEditView.getText()) || TextUtils.isEmpty(quantityEditView.getText()) || TextUtils.isEmpty(priceEditView.getText())){
                    Intent replyIntent = new Intent();
                    setResult(RESULT_CANCELED, replyIntent);
                    finish();
                }
                else {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_REPLY_PRODUCT_NAME, productEditView.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY_PRICE, Double.parseDouble(priceEditView.getText().toString()));
                    replyIntent.putExtra(EXTRA_REPLY_QUANTITY, Integer.parseInt(quantityEditView.getText().toString()));
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(2).setEnabled(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }



}
