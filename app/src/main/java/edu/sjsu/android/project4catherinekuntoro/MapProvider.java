package edu.sjsu.android.project4catherinekuntoro;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MapProvider extends ContentProvider {

    private final static String AUTHORITY = "edu.sjsu.android.project4catherinekuntoro";
    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private MapsDB database;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return database.deleteAll();
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.
        long rowID = database.insert(values);

        if(rowID > 0){ // Success
            Uri _uri = ContentUris.withAppendedId(uri, rowID);
            // Call nofityChange() using the new URI
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        } else {
            throw new SQLException("Failed to add a record into " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        // Implement this to initialize your content provider on startup.
        database = new MapsDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Implement this to handle query requests from clients
        return database.getAllData();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}