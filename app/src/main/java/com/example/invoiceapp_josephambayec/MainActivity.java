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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.invoiceapp_josephambayec.ListAdapters.CustomerListAdapter;
import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_CUSTOMER_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_CUSTOMER_ACTIVITY_REQUEST_CODE = 2;
    public static final int SHOW_CUSTOMER_ACTIVITY_REQUEST_CODE = 3;
    public static final int NEW_ADDRESS_ACTIVITY_REQUEST_CODE = 4;
    public static final int SHOW_INVOICES_ACTIVITY_REQUEST_CODE = 5;

    public static final String EXTRA_DATA_UPDATE_CUSTOMER = "extra_customer_to_be_updated";
    public static final String EXTRA_DATA_SHOW_CUSTOMER = "extra_customer_to_be_shown";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    public static final String EXTRA_DATA_ADDRESS_ID = "extra_data_address_id";
    public static final String EXTRA_REPLY_ID = "com.android.example.invoiceapp_josephambayec.REPLY_ID";

    private CustomerViewModel mCustomerViewModel;
    private AddressViewModel mAddressViewModel;

    private CustomerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Section
        // RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new CustomerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // CustomerViewModel
        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        mCustomerViewModel.getAllCustomers().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(@Nullable final List<Customer> customers) {
                adapter.setCustomers(customers);
            }
        });

        mAddressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        // Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCustomerActivity.class);
                startActivityForResult(intent, NEW_CUSTOMER_ACTIVITY_REQUEST_CODE);
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
                        Customer customer = adapter.getCustomerAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.delete_customer_preamble) + " " +
                                        customer.getName(), Toast.LENGTH_LONG).show();

                        // Delete the customer.
                        System.out.println(mCustomerViewModel.customerGetDefaultAddress(customer));
                        mCustomerViewModel.deleteCustomer(customer);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
        // On Item Click
        adapter.setOnItemClickListener(new CustomerListAdapter.ClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Customer customer = adapter.getCustomerAtPosition(position);
                launchUpdateCustomerActivity(customer);
            }
        });

        adapter.setOnButtonItemClickListener(new CustomerListAdapter.ClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Customer customer = adapter.getCustomerAtPosition(position);
                launchShowInvoicesActivity(customer);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();

            // Delete the existing data.
            mCustomerViewModel.deleteAll();

            // Set blank array
            adapter.setCustomers(new ArrayList<Customer>());
            return true;
        }
        else if (id == R.id.settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the user enters a new customer in the NewCustomerActivity,
     * that activity returns the result to this activity.
     * If the user entered a new customer, save it in the database.
     *
     * @param requestCode ID for the request
     * @param resultCode  indicates success or failure
     * @param data        The Intent sent back from the NewCustomerActivity,
     *                    which includes the customer that the user entered
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CUSTOMER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EXTRA_REPLY_ID, -1);
            if (id != 0 && id != -1){
                Customer customer = mCustomerViewModel.getCustomerById(id);
                if (customer.getName().equals(""))
                    customer.setName(data.getStringExtra(NewCustomerActivity.EXTRA_REPLY));
                mCustomerViewModel.update(customer);
            }
            else {
                mCustomerViewModel.insert(new Customer(data.getStringExtra(NewCustomerActivity.EXTRA_REPLY), -1));
            }


        } else if (requestCode == UPDATE_CUSTOMER_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewCustomerActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewCustomerActivity.EXTRA_REPLY_ID, -1);
            int defaultAddressId = data.getIntExtra(NewCustomerActivity.EXTRA_DATA_ADDRESS_ID, -1);
            if (id != -1) {
                mCustomerViewModel.update(new Customer(id, name, defaultAddressId));
            } else {
                Toast.makeText(this, R.string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            int id = data.getIntExtra(EXTRA_REPLY_ID, -1);
            Customer customer = mCustomerViewModel.getCustomerById(id);
            if (customer.getName().equals("")){
                mCustomerViewModel.deleteCustomer(mCustomerViewModel.getCustomerById(id));
                Toast.makeText(
                        this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            }

        }
    }


    private void launchUpdateCustomerActivity(Customer customer) {
        Intent intent = new Intent(this, EditCustomerActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_CUSTOMER, customer.getName());
        intent.putExtra(EXTRA_DATA_ID, customer.getId());
        intent.putExtra(EXTRA_DATA_ADDRESS_ID, customer.getDefaultAddressId());
        startActivityForResult(intent, UPDATE_CUSTOMER_ACTIVITY_REQUEST_CODE);
    }

    private void launchShowInvoicesActivity(Customer customer) {
        Intent intent = new Intent(this, ShowInvoicesActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_CUSTOMER, customer.getName());
        intent.putExtra(EXTRA_DATA_ID, customer.getId());
        intent.putExtra(EXTRA_DATA_ADDRESS_ID, customer.getDefaultAddressId());
        startActivityForResult(intent, SHOW_INVOICES_ACTIVITY_REQUEST_CODE);
    }


}