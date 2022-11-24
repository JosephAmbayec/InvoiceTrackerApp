package com.example.invoiceapp_josephambayec;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class EditCustomerActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.invoiceapp_josephambayec.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    public static final String EXTRA_DATA_UPDATE_CUSTOMER = "extra_customer_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_ADDRESS_ID = "extra_data_address_id";
    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;

    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;
    private EditText mEditCustomerNameView;
    private Spinner dropdown;
    private Bundle extras;
    private List<Address> currentAddressList = new ArrayList<>();
    private int currentCustomerId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        mEditCustomerNameView = findViewById(R.id.customerName);

        int id = -1;

        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        dropdown = findViewById(R.id.addressDropdown);


        extras = getIntent().getExtras();

        if (extras != null) {
            currentCustomerId = extras.getInt(EXTRA_DATA_ID);
            String customerName = extras.getString(EXTRA_DATA_UPDATE_CUSTOMER, "");
            if (!customerName.isEmpty()) {
                mEditCustomerNameView.setText(customerName);
                mEditCustomerNameView.setSelection(customerName.length());
                mEditCustomerNameView.requestFocus();
            }
            reloadDropdown(extras, dropdown);
        }

        if (currentAddressList.size() == 0)
            dropdown.setVisibility(View.GONE);


        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Create a new Intent for the reply.
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditCustomerNameView.getText())) {
                    // No customer name was entered, set the result accordingly.
                    replyIntent.putExtra(EXTRA_REPLY_ID, currentCustomerId);
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    // Get the new customer
                    // This will be replaced with a new Customer() instead;
                    String customerName = mEditCustomerNameView.getText().toString();
                    // Add new Customer to extras
                    replyIntent.putExtra(EXTRA_REPLY, customerName);
                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                        Customer oldCustomer = mCustomerViewModel.getCustomerById(extras.getInt(EXTRA_DATA_ID));
                        int defaultValue = ((Address) dropdown.getSelectedItem()).getId();
                        replyIntent.putExtra(EXTRA_DATA_ADDRESS_ID, defaultValue);
                        mCustomerViewModel.update(new Customer(oldCustomer.getId(), customerName, defaultValue));
                        int id = extras.getInt(EXTRA_DATA_ID, -1);

                        if (id != -1 && defaultValue != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, id);
                        }
                        else{
                            // Set the result status to indicate success.
                            setResult(RESULT_OK, replyIntent);
                        }

                    }
                    else {
                        replyIntent.putExtra(EXTRA_REPLY_ID, currentCustomerId);

                        replyIntent.putExtra(EXTRA_REPLY, customerName);
                    }
                    // Set the result status to indicate success.
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        Button createAddressButton = findViewById(R.id.createAddress_Button);
        createAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewAddressActivity(currentCustomerId);
            }
        });

        Button editAddressButton = findViewById(R.id.editAddress_Button);
        editAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditAddressActivity(currentCustomerId,((Address)dropdown.getSelectedItem()).getId());
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ADDRESS_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // address = new Customer(data.getStringExtra(NewCustomerActivity.EXTRA_REPLY), 1);
            // Save the data.
           // mAddressViewModel.insert(address);
            reloadDropdown(dropdown, data.getIntExtra(NewAddressActivity.EXTRA_REPLY_ID, 0));
            Customer currentCustomer = mCustomerViewModel.getCustomerById(currentCustomerId);
            currentCustomer.setDefaultAddressId(data.getIntExtra(NewAddressActivity.EXTRA_REPLY_ID, -1));
            mCustomerViewModel.update(currentCustomer);
        }
        else {
            Toast.makeText(
                    this, "Address could not be saved", Toast.LENGTH_LONG).show();
        }
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

    private void launchNewAddressActivity(int customerId){
        Intent intent = new Intent(this, NewAddressActivity.class);
        intent.putExtra(EXTRA_DATA_ID, customerId);
        startActivityForResult(intent, NEW_ADDRESS_ACTIVITY_REQUEST_CODE);
    }

    private void launchEditAddressActivity(int customerId, int selectedAddressId){
        Intent intent = new Intent(this, NewAddressActivity.class);
        intent.putExtra(EXTRA_DATA_ID, customerId);
        intent.putExtra(EXTRA_DATA_ADDRESS_ID, selectedAddressId);
        startActivityForResult(intent, NEW_ADDRESS_ACTIVITY_REQUEST_CODE);
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

    private void reloadDropdown(Spinner dropdown, int id){
        List<Address> addressList = mAddressViewModel.getAddressesByCustomerId(currentCustomerId);
        currentAddressList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < addressList.size(); i++){
            currentAddressList.add(addressList.get(i));
            if (addressList.get(i).getId() == id);
                index = i;
        }

        ArrayAdapter<Address> adapter = new ArrayAdapter<Address>(this, android.R.layout.simple_spinner_item, currentAddressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(index);
        dropdown.setVisibility(View.VISIBLE);
    }
}
