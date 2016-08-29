package com.example.android.p10inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by aspun_000 on 8/29/2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InventoryStore";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("QUERY:",DbContract.Table.CREATE_TABLE );
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If a table with same name is already existed or modified like updating
        // the table name or anything, this will delete the previous one and will
        // create again
        deleteTable(db);
        //It will create the tables again.
        createTable(db);
    }

    /**
     CRUD(Create, Read, Update, Delete)
     **/
    public void createTable(SQLiteDatabase db){
        db.execSQL(DbContract.Table.CREATE_TABLE);
    }

    public void deleteTable(SQLiteDatabase db){
        db.execSQL(DbContract.Table.DELETE_TABLE);
    }

    void addItem(Inventory inventoryItem){
        //Create a Database Connection
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.Table.KEY_NAME, inventoryItem.getName());
        values.put(DbContract.Table.KEY_QUANTITY, inventoryItem.getQuantity());
        values.put(DbContract.Table.KEY_PRICE, inventoryItem.getPrice());
//        values.put(DbContract.Table1.KEY_IMAGE, newHabit.getImagePath());
        // Inserting Row
        db.insert(DbContract.Table.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public void updateInventoryRow(double rowId, Inventory inv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.Table.KEY_NAME,inv.getName());
        values.put(DbContract.Table.KEY_PRICE,inv.getPrice());
        values.put(DbContract.Table.KEY_QUANTITY,inv.getQuantity());
        db.update(DbContract.Table.TABLE_NAME, values, DbContract.Table.KEY_ID + "=" + rowId,null);
    }

    // Deleting single contact
    public void deleteHabitRow(double rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbContract.Table.TABLE_NAME, DbContract.Table.KEY_ID + "=" + rowId, null);
    }

    public long rowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count  = DatabaseUtils.queryNumEntries(db,DbContract.Table.TABLE_NAME );
        db.close();
        return count;
    }

    public ArrayList<Inventory> readInventory(){
        ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
        String query = "SELECT * FROM " + DbContract.Table.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int quantity = cursor.getInt(2);
                double price = cursor.getDouble(3);

                Inventory item = new Inventory(id, name, quantity, price);
                inventoryList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return inventoryList;
    }

}