package edu.sjsu.android.project4catherinekuntoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MapsDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mapDatabase";
    private static final int    VERSION = 1;

    // Table for database
    protected static final String TABLE_NAME = "map";
    protected static final String ID = "_id"; // column 1; primary key
    protected static final String LATITUDE = "latitude"; // column 2
    protected static final String LONGITUDE = "longitude"; // column 3
    protected static final String ZOOM_LEVEL = "zoom_level"; // column 4

    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LATITUDE + " DOUBLE NOT NULL, "
            + LONGITUDE + " DOUBLE NOT NULL, "
            + ZOOM_LEVEL + " FLOAT NOT NULL);";


    public MapsDB(@Nullable Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insert(ContentValues contentValues){
        SQLiteDatabase database = getWritableDatabase();
        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int deleteAll(){
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(TABLE_NAME, null, null);
    }

    public Cursor getAllData(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_NAME,
                new String[]{ID, LATITUDE, LONGITUDE, ZOOM_LEVEL},
                null, null, null, null, null);
    }
}
