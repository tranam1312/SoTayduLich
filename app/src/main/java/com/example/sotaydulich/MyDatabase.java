package com.example.sotaydulich;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.Editable;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SoTayDuLich";
    private static final String TABLE_NAME = "Dulich";
    private static final String COL_1 = "id";
    private static final String COL_2 = "diadiem";
    private  static  final  String COL_3 = "mota";
    private  static  final  String COL_4 = "uriImg";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " + COL_3 + " TEXT, "+ COL_4 + " TEXT)");
        sqLiteDatabase.execSQL("create table " + "User" + " (" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email" + " TEXT, " + "pass" + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "User");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData( String diadiem, String mota, byte[] byteImg){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, diadiem);
        values.put(COL_3, mota);
        values.put(COL_4, byteImg);
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateData(String id, String diadiem, String mota, byte[] bytes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, diadiem);
        values.put(COL_3, mota);
        values.put(COL_4, bytes);
        long result = db.update(TABLE_NAME, values, COL_1 + "=?", new String[]{id});
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL_1 + "=?", new String[]{id});
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }
    public boolean insertDataUser( String email, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("pass", pass);
        long result = db.insert("User", null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean updateDataUser(String id, String email, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put("User", email);
        values.put("pass", pass);
        long result = db.update("User", values, COL_1 + "=?", new String[]{id});
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getAllDataUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + "User", null);
        return cursor;
    }
}