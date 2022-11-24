package com.example.invoiceapp_josephambayec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import android.content.Context;

import com.example.invoiceapp_josephambayec.DAOs.CustomerDao;

import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static CustomerRoomDatabase db;
    private static CustomerDao customerDao;


    @Before
    public static void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CustomerRoomDatabase.class).build();
        customerDao = db.customerDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void addition_isFalse() {
        assertFalse(4 == 2 + 3);
    }

}