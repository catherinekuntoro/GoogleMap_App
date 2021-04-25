package edu.sjsu.android.project4catherinekuntoro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LoaderManager.getInstance(this).restartLoader(0, null,  this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                Background background = new Background();

                ContentValues contentValues = new ContentValues();
                contentValues.put(MapsDB.LATITUDE, latLng.latitude);
                contentValues.put(MapsDB.LONGITUDE, latLng.longitude);
                contentValues.put(MapsDB.ZOOM_LEVEL, mMap.getCameraPosition().zoom);

                //background.doInBackground(contentValues);
                background.execute(contentValues);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Background background = new Background();
                background.execute((ContentValues) null);
                //background.doInBackground(null);
                mMap.clear(); // clear all the markers
            }
        });
    }

    public void getLocation(View view) {
        GPSTracker tracker = new GPSTracker(this);
        tracker.getLocation();
    }

    public void switchView(View view) {
        CameraUpdate update = null;

        if (view.getId() == R.id.city_button) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10f);
        } else if (view.getId() == R.id.university_button) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14f);
        } else if (view.getId() == R.id.cs_button) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18f);
        }
        mMap.animateCamera(update);
    }

    //?????
    public void uninstall(View view) {
        Intent delete = new Intent(Intent.ACTION_DELETE,
                Uri.parse("package:" + getPackageName()));
        startActivity(delete);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, MapProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.moveToFirst()){
            Log.i("TAG" , Integer.toString(cursor.getCount()));
            do{
                Double latitude = (cursor.getDouble(cursor.getColumnIndex(MapsDB.LATITUDE)));
                Double longitude = (cursor.getDouble(cursor.getColumnIndex(MapsDB.LONGITUDE)));

                LatLng saved = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(saved));
            } while(cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private class Background extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            if (contentValues[0] == null) {
                // delete
                getContentResolver().delete(MapProvider.CONTENT_URI, null, null);
            } else {
                //insert the first contentValues
                getContentResolver().insert(MapProvider.CONTENT_URI, contentValues[0]);
            }
            return null;
        }

    }
}