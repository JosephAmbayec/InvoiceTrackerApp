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
import android.widget.Toast;

import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class NewAddressActivity extends AppCompatActivity {
    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_ADDRESS_ID = "extra_data_address_id";
    public static final String EXTRA_REPLY_ADDRESS_ID = "extra_data_address_id";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";
    private AddressViewModel mAddressViewModel;
    private EditText mStreetView;
    private EditText mCityView;
    private EditText mProvinceView;
    private EditText mCountryView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        mStreetView = findViewById(R.id.customerName);
        mCityView = findViewById(R.id.addressDropdown);
        mProvinceView = findViewById(R.id.provinceName);
        mCountryView = findViewById(R.id.countryName);

        Bundle extras = getIntent().getExtras();
        Button button = findViewById(R.id.button_save);

        int address_id = extras.getInt(EXTRA_DATA_ADDRESS_ID, -1);
        if (address_id != -1){
            setTitle("Edit Address");
            Address address = mAddressViewModel.getAddressById(address_id);
            mStreetView.setText(address.getStreet());
            mCityView.setText(address.getCity());
            mProvinceView.setText(address.getProvince());
            mCountryView.setText(address.getCountry());
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mStreetView.getText()) || TextUtils.isEmpty(mCityView.getText()) || TextUtils.isEmpty(mProvinceView.getText()) || TextUtils.isEmpty(mCountryView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String streetName = mStreetView.getText().toString();
                    String cityName = mCityView.getText().toString();
                    String provinceName = mProvinceView.getText().toString();
                    String countryName = mCountryView.getText().toString();
                    Address address = null;

                    if (address_id != -1){
                        address = mAddressViewModel.getAddressById(address_id);
                        address.setStreet(streetName);
                        address.setCity(cityName);
                        address.setProvince(provinceName);
                        address.setCountry(countryName);
                        mAddressViewModel.update(address);
                        replyIntent.putExtra(EXTRA_REPLY_ID, address_id);
                    }
                    else {

                        int id = extras.getInt(EXTRA_DATA_ID, -1);
                        if (id != -1) {
                            address = new Address(id, streetName, cityName, provinceName, countryName);
                        } else
                            setResult(RESULT_CANCELED);

                        if (address != null) {
                            mAddressViewModel.insert(address);
                            List<Address> temp = mAddressViewModel.getAddressesByCustomerId(extras.getInt(EXTRA_DATA_ID));
                            replyIntent.putExtra(EXTRA_REPLY_ID, temp.get(temp.size()-1).getId());
                        }
                    }


                    setResult(RESULT_OK, replyIntent);
                }
                finish();
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
}
