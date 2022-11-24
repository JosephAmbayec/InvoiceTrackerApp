package com.example.invoiceapp_josephambayec;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.invoiceapp_josephambayec.DAOs.AddressDao;
import com.example.invoiceapp_josephambayec.DAOs.CustomerDao;
import com.example.invoiceapp_josephambayec.DAOs.InvoiceDao;
import com.example.invoiceapp_josephambayec.DAOs.InvoiceDetailDao;
import com.example.invoiceapp_josephambayec.entities.Address;
import com.example.invoiceapp_josephambayec.entities.Customer;
import com.example.invoiceapp_josephambayec.entities.Invoice;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.util.ArrayList;
import java.util.List;


@Database(entities = {Customer.class, Address.class, Invoice.class, InvoiceDetail.class}, version = 14, exportSchema = false)
public abstract class CustomerRoomDatabase extends RoomDatabase {

    public abstract CustomerDao customerDao();
    public abstract AddressDao addressDao();
    public abstract InvoiceDao invoiceDao();
    public abstract InvoiceDetailDao invoiceDetailDao();

    private static CustomerRoomDatabase INSTANCE;

    public static CustomerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CustomerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CustomerRoomDatabase.class, "customer_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CustomerDao customerDao;
        private final AddressDao addressDao;
        private final InvoiceDao invoiceDao;
        private final InvoiceDetailDao invoiceDetailDao;
        
        private static String [] names = {"Joseph", "Gabriel", "Ricky"};

        private static Address[] addressesCustomer1 = { new Address(1, "213 Main Street", "Toronto", "Ontario", "Canada"),
                new Address(1, "42 Eastview Road", "Ottawa", "Ontario", "Canada"),
                new Address(1, "219 Hymus Avenue", "Pointe-Claire", "Quebec", "Canada")
        };

        private static Address[] addressesCustomer2 = { new Address(2, "15 South Street", "Toronto", "Ontario", "Canada"),
                new Address(2, "46 Westview Road", "Ottawa", "Ontario", "Canada"),
                new Address(2, "29 Bruinswick Avenue", "Pointe-Claire", "Quebec", "Canada")
        };

        private static Address[] addressesCustomer3 = { new Address(3, "12 Rue Richmond", "Pierrefonds", "Quebec", "Canada"),
                new Address(3, "21 Terry Fox Street", "Kirkland", "Quebec", "Canada"),
                new Address(3, "54 Lakeshore Road", "Pointe-Claire", "Quebec", "Canada")
        };

        PopulateDbAsync(CustomerRoomDatabase db) {
            customerDao = db.customerDao();
            addressDao = db.addressDao();
            invoiceDao = db.invoiceDao();
            invoiceDetailDao = db.invoiceDetailDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (customerDao.getAnyCustomer().length < 1){

                Customer customer = new Customer(names[0], 1);
                customer.setId(1);
                customerDao.insert(customer);

                customer = new Customer(names[1], 4);
                customer.setId(2);
                customerDao.insert(customer);

                customer = new Customer(names[2], 7);
                customer.setId(3);
                customerDao.insert(customer);
                //addressDao.deleteAll();
            }

            if (addressDao.getAnyAddress().length < 1){

                for (int i = 0; i <= addressesCustomer1.length - 1; i++) {
                    Address address = addressesCustomer1[i];
                    address.setId(i + 1);
                    addressDao.insert(address);
                }

                for (int i = 0; i <= addressesCustomer2.length - 1; i++) {
                    Address address = addressesCustomer2[i];
                    address.setId(addressesCustomer2.length + i + 1);
                    addressDao.insert(address);
                }

                for (int i = 0; i <= addressesCustomer3.length - 1; i++) {
                    Address address = addressesCustomer3[i];
                    address.setId(addressesCustomer2.length + addressesCustomer3.length + i + 1);
                    addressDao.insert(address);
                }
                List<Invoice> invoiceList = new ArrayList<Invoice>();
                invoiceList.add(new Invoice(1, 1, addressesCustomer1[0].toString(), 0));
                invoiceList.add(new Invoice(2, 1, addressesCustomer1[1].toString(), 0));
                invoiceList.add(new Invoice(3, 1, addressesCustomer1[1].toString(), 0));

                invoiceList.add(new Invoice(4, 2, addressesCustomer2[1].toString(), 0));
                invoiceList.add(new Invoice(5, 2, addressesCustomer2[2].toString(), 0));
                invoiceList.add(new Invoice(6, 2, addressesCustomer2[0].toString(), 0));

                invoiceList.add(new Invoice(7, 3, addressesCustomer3[1].toString(), 0));
                invoiceList.add(new Invoice(8, 3, addressesCustomer3[2].toString(), 0));
                invoiceList.add(new Invoice(9, 3, addressesCustomer3[1].toString(), 0));
                invoiceList.add(new Invoice(10, 3, addressesCustomer3[0].toString(), 0));


                List<InvoiceDetail> invoiceDetailList = new ArrayList<InvoiceDetail>();
                invoiceDetailList.add(new InvoiceDetail(1, "5 Dollar Steam Card", 5, 2));
                invoiceDetailList.add(new InvoiceDetail(1, "USB-C Charger", 5.50, 1));

                invoiceDetailList.add(new InvoiceDetail(2, "Corsair k70 Keyboard", 100, 1));
                invoiceDetailList.add(new InvoiceDetail(2, "Keycap Puller", 23.60, 1));

                invoiceDetailList.add(new InvoiceDetail(3, "Blue Yeti Microphone", 100, 1));
                invoiceDetailList.add(new InvoiceDetail(3, "Logitech G604", 70.60, 1));

                invoiceDetailList.add(new InvoiceDetail(4, "Speciallized KeyCaps", 0.50, 5));
                invoiceDetailList.add(new InvoiceDetail(4, "Screen Protector", 19.99, 1));

                invoiceDetailList.add(new InvoiceDetail(5, "Airpods", 200, 1));
                invoiceDetailList.add(new InvoiceDetail(5, "Airtag", 50.00, 1));

                invoiceDetailList.add(new InvoiceDetail(6, "Corsair k70 Keyboard", 100, 1));
                invoiceDetailList.add(new InvoiceDetail(6, "Keycap Puller", 23.60, 1));

                invoiceDetailList.add(new InvoiceDetail(7, "Sennheiser G4ME One Headset", 250, 1));
                invoiceDetailList.add(new InvoiceDetail(7, "LED Light", 5.00, 5));

                invoiceDetailList.add(new InvoiceDetail(8, "Logitech Webcam", 85, 1));
                invoiceDetailList.add(new InvoiceDetail(8, "WiFi Network Adapter", 30.50, 1));

                invoiceDetailList.add(new InvoiceDetail(9, "ASUS Monitor", 400, 2));
                invoiceDetailList.add(new InvoiceDetail(9, "Bluetooth Speaker", 80.00, 1));

                invoiceDetailList.add(new InvoiceDetail(10, "iPhone 11", 1000, 1));
                invoiceDetailList.add(new InvoiceDetail(10, "Xbox Controller", 39.99, 1));



                for (Invoice invoice : invoiceList){
                    double totalForInvoice = 0;
                    for (InvoiceDetail invoiceDetail : invoiceDetailList){
                        invoiceDetail.setLineTotal((double)invoiceDetail.getQuantity() * invoiceDetail.getPricePerUnit());
                        if (invoice.getId() == invoiceDetail.getInvoiceId())
                            totalForInvoice += invoiceDetail.getLineTotal();
                    }
                    invoice.setTotal(totalForInvoice);
                }

                for (Invoice invoice : invoiceList) {
                    invoiceDao.insert(invoice);
                }

                for (InvoiceDetail invoiceDetail : invoiceDetailList){
                    invoiceDetailDao.insert(invoiceDetail);
                }
            }


            return null;
        }
    }
}

