package com.example.salthash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.UnsupportedEncodingException;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_USER = "Username";
    private static final String COLUMN_PASS = "Password";
    private static final String COLUMN_SALT = "Salt";
    SQLiteDatabase database;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_USER +
            " text primary key not null, " + COLUMN_PASS + " text not null, " + COLUMN_SALT +
            " text not null);";

    public DatabaseHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public void drop() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        database.execSQL(query);
        onCreate(database);
    }

    public void insertUser(User u) throws UnsupportedEncodingException{
        database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER, u.getUsername());
        cv.put(COLUMN_PASS, new String(u.getPassword(), "UTF8"));
        cv.put(COLUMN_SALT, new String(u.getSalt(), "UTF8"));
        database.insert(TABLE_NAME, null, cv);
    }

    public String[] checkUser(String user) {
        database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER + ", " + COLUMN_PASS + ", " + COLUMN_SALT + " from " +
                TABLE_NAME;
        Cursor c = database.rawQuery(query, null);
        String dbUser, dbPass, dbSalt;
        dbPass = "NULL";
        dbSalt = "";
        if(c.moveToFirst()) {
            do {
                dbUser = c.getString(0);
                if(dbUser.equals(user)) {
                    dbPass = c.getString(1);
                    dbSalt = c.getString(2);
                }
            } while(c.moveToNext());
        }
        c.close();
        return new String[] {dbPass, dbSalt};//s;
    }
}
