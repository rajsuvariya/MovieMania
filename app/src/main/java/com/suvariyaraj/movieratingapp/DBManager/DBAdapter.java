package com.suvariyaraj.movieratingapp.DBManager;

/**
 * Created by GOODBOY-PC on 14/05/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.database.*;
import android.util.Log;
import android.widget.Toast;

import com.suvariyaraj.movieratingapp.CustomListView.Helper;

import java.util.ArrayList;

public class DBAdapter{

    DBHelper dbHelper;

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insert(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DBHelper.UID, id);
        long raw = db.insert(DBHelper.TABLE_NAME, null, value);
//        Log.d("databaseHelp", "insert called : raw : "+raw+", ID : "+id);
    }

    public boolean isFav (int id){
        boolean isfav = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String []columns = {dbHelper.UID};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, dbHelper.UID + "="+id,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(dbHelper.UID);
            int ID = cursor.getInt(index);
            buffer.append("ID : "+id+"\n");
            isfav=true;
        }
//        Log.d("databaseHelp", String.valueOf(buffer));
        return isfav;
    }

    public ArrayList<Integer> getAllFavouriteMovie(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String []columns = {dbHelper.UID};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns,null,null,null,null,null);
//        StringBuffer buffer = new StringBuffer();
        ArrayList<Integer> buffer = new ArrayList<Integer>();
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(dbHelper.UID);
            int ID = cursor.getInt(index);
            buffer.add(ID);
        }
//        Log.d("databaseHelp", String.valueOf(buffer));
//        return String.valueOf(buffer);
        return buffer;
    }

    public void remove (int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long raw = db.delete(dbHelper.TABLE_NAME, dbHelper.UID+"="+id, null);
//        Log.d("databaseHelp", "deleted called : raw : " + raw + ", ID : " + id);
    }

    static class DBHelper extends SQLiteOpenHelper{

        private static final String DATABSE_NAME = "moviemania";
        private static final String TABLE_NAME = "favmovie";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "id";
        private Context context;

        public DBHelper(Context context) {
            super(context, DATABSE_NAME, null, DATABASE_VERSION);
            Toast.makeText(context, "Constructor Called", Toast.LENGTH_LONG);
//            Log.d("databaseHelp", "Constructor Called");
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY );");
//                Log.d("databaseHelp", "OnCreate Called");
                Toast.makeText(context, "OnCreate Called", Toast.LENGTH_SHORT);
            } catch (SQLiteException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE " + TABLE_NAME + " IF EXISTS;");
//                Log.d("databaseHelp", "OnUpgrade Called");
                Toast.makeText(context, "OnUpgrade Called", Toast.LENGTH_SHORT);
                onCreate(db);
            } catch (SQLiteException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT);
            }
        }

    }
}
