package com.example.invoiceapp_josephambayec;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.InvoiceViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;

import java.util.ArrayList;
import java.util.List;

public class EditInvoiceActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.invoiceapp_josephambayec.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    public static final String EXTRA_REPLY_ADDRESS_ID = "com.android.example.invoiceapp_josephambayec._REPLY_ADDRESS_ID";
    public static final String EXTRA_DATA_UPDATE_CUSTOMER = "extra_customer_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_ADDRESS_ID = "extra_data_address_id";
    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;

    private InvoiceViewModel mInvoiceViewModel;
    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;
    private Spinner dropdown;
    private Bundle extras;
    private List<Address> currentAddressList = new ArrayList<>();
    private int currentCustomerId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invoice);

        int id = -1;

        mInvoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        dropdown = findViewById(R.id.addressDropdown);
        Button button = findViewById(R.id.button_save);

        extras = getIntent().getExtras();

        if (extras != null) {
            currentCustomerId = extras.getInt(EXTRA_DATA_ID);
            reloadDropdown(extras, dropdown);
        }

        if (currentAddressList.size() == 0) {
            List<String> singleString = new ArrayList<>();
            singleString.add("Customer has no addresses.");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, singleString);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);
            dropdown.setSelection(0);
            dropdown.setVisibility(View.VISIBLE);
            button.setEnabled(false);
        }
        else {
            button.setEnabled(true);
        }



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY_ADDRESS_ID, ((Address) dropdown.getSelectedItem()).getId());
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY_ID, currentCustomerId);
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }


    private void reloadDropdown(Bundle extras, Spinner dropdown){

        int tempId = mCustomerViewModel.customerGetDefaultAddress(mCustomerViewModel.getCustomerById(currentCustomerId));
        List<Address> addressList = mAddressViewModel.getAddressesByCustomerId(currentCustomerId);
        int index = 0;
        currentAddressList = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++){
            currentAddressList.add(addressList.get(i));
            if (addressList.get(i).getId() == tempId)
                index = i;
        }

        ArrayAdapter<Address> adapter = new ArrayAdapter<Address>(this, android.R.layout.simple_spinner_item, currentAddressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(index);
        dropdown.setVisibility(View.VISIBLE);
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
}
