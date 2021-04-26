package com.burak.heatmap;

import androidx.annotation.RawRes;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<LatLng> latLngs = null;
    private Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        try {
            latLngs = readItems(R.raw.hot_points);
        } catch (JSONException e){
            Toast.makeText(this,"Lokasyon listesini okurken hata ile karşılaşıldı",Toast.LENGTH_SHORT).show();
        }
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().data(latLngs).build();

        TileOverlay overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));


        //mMap.addMarker(new MarkerOptions().position().title("Marker in Sydney"));

    }

    private List<LatLng> readItems(@RawRes int resource) throws JSONException{
        List<LatLng> result = new ArrayList<>();
        InputStream inputStream = mcontext.getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);

        for (int i =0; i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            result.add(new LatLng(lat,lng));
        }
        return result;
    }
}