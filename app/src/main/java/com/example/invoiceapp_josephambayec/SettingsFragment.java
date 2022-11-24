package com.example.invoiceapp_josephambayec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.invoiceapp_josephambayec.ViewModels.AddressViewModel;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        EditTextPreference editTextPreference = (EditTextPreference) getPreferenceManager().findPreference("company_name_key");
        editTextPreference.setSummaryProvider(new Preference.SummaryProvider() {
            @Override
            public CharSequence provideSummary(Preference preference) {
                return editTextPreference.getText().toString();
            }
        });

        EditTextPreference numberTextPreference = (EditTextPreference) getPreferenceManager().findPreference("employee_number_key") ;
        numberTextPreference.setSummaryProvider(new Preference.SummaryProvider() {
            @Override
            public CharSequence provideSummary(Preference preference) {
                return numberTextPreference.getText().toString();
            }
        });


        ListPreference listPreference = (ListPreference) getPreferenceManager().findPreference("company_type_preference");
        listPreference.setSummaryProvider(new Preference.SummaryProvider() {
            @Override
            public CharSequence provideSummary(Preference preference) {
                return listPreference.getValue().toString();
            }
        });


    }
}