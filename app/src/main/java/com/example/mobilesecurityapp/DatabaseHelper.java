package com.example.mobilesecurityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHelper extends SQLiteOpenHelper {

    // get an instance of SQLiteDatabase
    private SQLiteDatabase writableDatabase = getWritableDatabase();
    private SQLiteDatabase readableDatabase = getReadableDatabase();

    // these are NOT CASE SENSITIVE
    private static final String DATABASE_NAME = "WeatherApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "city";
    private static final String ID_COLUMN = "id";
    private static final String NEIGHBORHOOD_COLUMN = "neighborhood";
    private static final String CITY_COLUMN = "city";
    private static final String STATE_COLUMN = "state";
    private static final String COUNTRY_ISO_CODE_COLUMN = "country_iso_code";
    private static final String LATITUDE_COLUMN = "latitude";
    private static final String LONGITUDE_COLUMN = "longitude";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            ID_COLUMN + " INTEGER PRIMARY KEY," +
            NEIGHBORHOOD_COLUMN + " TEXT," +
            CITY_COLUMN + " TEXT," +
            STATE_COLUMN + " TEXT," +
            COUNTRY_ISO_CODE_COLUMN + " TEXT," +
            LATITUDE_COLUMN + " REAL ," +
            LONGITUDE_COLUMN +" REAL)";

    // public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
    //                      int version)
    // this will create the database and tables
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean isAnyCitySaved(){
        int id = 1;
        Cursor resultSet = getAllData();

        while(resultSet.moveToNext()) {
            if ((resultSet.getInt(0) == 1)) {
                resultSet.close();
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    boolean insertData(City cityToInsert) {
        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_COLUMN, 1);
        contentValues.put(NEIGHBORHOOD_COLUMN, cityToInsert.getNeighborhood());
        contentValues.put(CITY_COLUMN, cityToInsert.getName());
        contentValues.put(STATE_COLUMN, cityToInsert.getState());
        contentValues.put(COUNTRY_ISO_CODE_COLUMN, cityToInsert.getCountryIsoCode());
        contentValues.put(LATITUDE_COLUMN, cityToInsert.getLatitude());
        contentValues.put(LONGITUDE_COLUMN, cityToInsert.getLongitude());
        // Insert the new row, returning the primary key value of the new row
        long returnValue = writableDatabase.insert(TABLE_NAME, null, contentValues);
        // or it will return -1 if there was an error inserting the data
        if(returnValue == -1){
            return false;
        }
        return true;
    }

    Cursor getAllData() {
        return writableDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void removeAllData() {
        writableDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(writableDatabase);
    }


    boolean updateData(City cityToUpdate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_COLUMN, 1);
        contentValues.put(NEIGHBORHOOD_COLUMN, cityToUpdate.getNeighborhood());
        contentValues.put(CITY_COLUMN, cityToUpdate.getName());
        contentValues.put(STATE_COLUMN, cityToUpdate.getState());
        contentValues.put(COUNTRY_ISO_CODE_COLUMN, cityToUpdate.getCountryIsoCode());
        contentValues.put(LATITUDE_COLUMN, cityToUpdate.getLatitude());
        contentValues.put(LONGITUDE_COLUMN, cityToUpdate.getLongitude());
        // We will only have one item in our database with the ID = 1
        long returnValue = writableDatabase.update(TABLE_NAME, contentValues,
                ID_COLUMN + " = ?", new String[] {"1"});
        return true;
    }

}
