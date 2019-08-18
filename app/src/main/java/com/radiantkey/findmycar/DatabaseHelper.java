package com.radiantkey.findmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mycar.db";
    public static final String R_TABLE_NAME = "record";
    public static final String R_COL_1 = "ID";
    public static final String R_COL_2 = "NAME";
    public static final String R_COL_3 = "NOTE";
    public static final String R_COL_4 = "TIME";
    public static final String R_COL_5 = "LAT";
    public static final String R_COL_6 = "LNG";
    public static final String R_COL_7 = "ALT";

//    public static final String H_TABLE_NAME = "history";
//    public static final String H_COL_1 = "ID";
//    public static final String H_COL_2 = "NAME";
//    public static final String H_COL_3 = "CATEGORY";
//    public static final String H_COL_4 = "START_TIME";
//    public static final String H_COL_5 = "LENGTH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + R_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, NOTE TEXT, TIME INTEGER, LAT DOUBLE, LNG DOUBLE, ALT DOUBLE)");
//        sqLiteDatabase.execSQL("create table " + H_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CATEGORY TEXT, START_TIME INTEGER, LENGTH INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + R_TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + H_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertData(String name, String cat, long time, double lat, double lng, double alt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(R_COL_2, name);
        contentValues.put(R_COL_3, cat);
        contentValues.put(R_COL_4, time);
        contentValues.put(R_COL_5, lat);
        contentValues.put(R_COL_6, lng);
        contentValues.put(R_COL_7, alt);

        long res = db.insert(R_TABLE_NAME, null, contentValues);
        return res;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + R_TABLE_NAME, null);
        return res;
    }

    public boolean updateData(long id, String name, String cat, long time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(R_COL_1, id);
        contentValues.put(R_COL_2, name);
        contentValues.put(R_COL_3, cat);
        contentValues.put(R_COL_4, time);
        db.update(R_TABLE_NAME, contentValues, "ID = ?", new String[] {String.valueOf(id)});
        return true;
    }

    public Integer deleteData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(R_TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});

    }

//    public long insertDataH(String name, String cat, long start, long tlength){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(H_COL_2, name);
//        contentValues.put(H_COL_3, cat);
//        contentValues.put(H_COL_4, start);
//        contentValues.put(H_COL_5, tlength);
//        return db.insert(H_TABLE_NAME, null, contentValues);
//    }
//
//    public Cursor getAllDataH(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from " + H_TABLE_NAME, null);
//        return res;
//    }
//
//    public Cursor getPortionH(long cond){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.query(H_TABLE_NAME, new String[]{"*"}, H_COL_4 + ">?", new String[]{Long.toString(cond)}, null, null, null);
//        return res;
//    }
//
//    public boolean updateDataH(String id, String name, String cat, int time){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(R_COL_1, id);
//        contentValues.put(R_COL_2, name);
//        contentValues.put(R_COL_3, cat);
//        contentValues.put(R_COL_4, time);
//        db.update(R_TABLE_NAME, contentValues, "ID = ?", new String[] {id});
//        return true;
//    }
//
//    public Integer deleteDataH(long id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(H_TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
//    }
}
