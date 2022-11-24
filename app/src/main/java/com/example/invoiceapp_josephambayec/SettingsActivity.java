package com.example.invoiceapp_josephambayec;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";
    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();
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
        if (id == R.id.home){
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}