package com.example.invoiceapp_josephambayec.generic;


import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public abstract class GenericDao<T> {
    private final String TABLE_NAME;

    public GenericDao(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(T object);

    @RawQuery
    public abstract int doDeleteAll(SupportSQLiteQuery query);

    public void deleteAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("DELETE FROM " + TABLE_NAME);
        doDeleteAll(query);
    }

    @Delete
    public abstract void delete(T object);

    @Update
    public abstract void update(T... object);


}
