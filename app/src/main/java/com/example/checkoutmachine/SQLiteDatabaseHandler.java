package com.example.checkoutmachine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;
import android.database.Cursor;


public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StoreDB";
    private static final String TABLE_NAME = "Store";
    private static final String KEY_NAME = "name";
    private static final String KEY_BARCODE = "barcode";
    private static final String KEY_PRICE = "price";
    private static final String[] COLUMNS = {KEY_BARCODE,KEY_NAME ,KEY_PRICE};

    public SQLiteDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Store ( "
                + "barcode TEXT PRIMARY KEY, " + "name TEXT, "
                + "price INTEGER )";
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(String barCode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "barcode = ?", new String[]{barCode});
        db.close();
    }

    public double getPrice(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                COLUMNS,
                "barCode = ?",
                new String[]{barCode},
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return Double.valueOf(cursor.getString(2));
    }

    public String getName(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                COLUMNS,
                "barCode = ?",
                new String[]{barCode},
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor.getString(1);
    }

    public boolean checkbarCode(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                COLUMNS,
                "barCode = ?",
                new String[]{barCode},
                null,
                null,
                null,
                null);
        return (cursor != null && cursor.moveToFirst());
    }

    public void addItem(String barCode, String name, double price){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, barCode);
        values.put(KEY_NAME, name);
        values.put(KEY_PRICE, price);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateItem(String barCode, String name, double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, barCode);
        values.put(KEY_NAME, name);
        values.put(KEY_PRICE, price);
        int i = db.update(TABLE_NAME, values, "barcode = ?", new String[]{barCode});
        db.close();
        return i;
    }

    public void cleanTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public String getAllElements(){

        String s = "";
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if(cursor.moveToFirst()){
                    do{
                        s += (cursor.getString(0) + " " + cursor.getString(1) + " " +
                                cursor.getString(2) + "\n");
                    }while(cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
                cursor.close();
            }
        } finally {
            db.close();
        }
        return s;
    }

}
