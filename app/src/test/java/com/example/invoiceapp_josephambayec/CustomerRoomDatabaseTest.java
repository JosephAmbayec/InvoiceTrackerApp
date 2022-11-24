package com.example.invoiceapp_josephambayec;

import static org.junit.Assert.*;

import androidx.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.invoiceapp_josephambayec.DAOs.CustomerDao;
import com.example.invoiceapp_josephambayec.ViewModels.CustomerViewModel;
import com.example.invoiceapp_josephambayec.entities.Customer;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CustomerRoomDatabaseTest {
    CustomerDao customerDao;
    CustomerRoomDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CustomerRoomDatabase.class).allowMainThreadQueries().build();
        customerDao = db.customerDao();
    }

    @After
    public void closeDb() throws IOException {
        customerDao.deleteAll();
        List<Customer> customerList = customerDao.getAll().getValue();
        db.close();
    }


    @Test
    public void insertCustomerNullAddress() {
        Customer customer = new Customer(1,"George", -1);
        customerDao.insert(customer);
        Customer byId = customerDao.getCustomerById(1);
        assertEquals(byId.getName(), customer.getName());
        assertEquals(byId.getId(), customer.getId());
        assertEquals(byId.getDefaultAddressId(), customer.getDefaultAddressId());
    }

    @Test
    public void insertCustomerFail() {
        Customer customer = new Customer(1,null, -1);
        customerDao.insert(customer);
        Customer byId = customerDao.getCustomerById(1);
        assertNull(byId);
    }

    @Test
    public void updateCustomerName(){
        Customer customer = new Customer(1, "Joseph", -1);
        customerDao.insert(customer);
        String name = "Billy";
        customer.setName(name);
        customerDao.update(customer);
        Customer updatedCustomer = customerDao.getCustomerById(1);
        assertEquals(name, updatedCustomer.getName());
    }

    @Test
    public void updateCustomerNameFail(){
        String initialName = "Joseph";
        Customer customer = new Customer(1, initialName, -1);
        customerDao.insert(customer);
        customer.setName(null);
        try {
            customerDao.update(customer);
        }
        catch (Exception e){
            Customer updatedCustomer = customerDao.getCustomerById(1);
            assertEquals(initialName, updatedCustomer.getName());
        }
    }

    @Test
    public void deleteCustomer(){
        String initialName = "Joseph";
        Customer customer = new Customer(1, initialName, -1);
        customerDao.insert(customer);
        Customer check = customerDao.getCustomerById(1);
        assertEquals(customer.getName(), check.getName());
        customerDao.delete(check);
        Customer check2 = customerDao.getCustomerById(1);
        assertNull(check2);
    }

    @Test
    public void deleteCustomerFail(){
        Customer customer = new Customer(1, "Joseph", -1);
        try {
            customerDao.delete(customer);
        }
        catch (Exception e){
            Customer updatedCustomer = customerDao.getCustomerById(1);
            assertNull(updatedCustomer);
        }
    }
}