package kakinada.smartcity.com.smartcitykakinada.googlemaps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kakinada.smartcity.com.smartcitykakinada.R;

public class MapboxMapViewActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Marker marker;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    static double latitude;
    static double longitude;
    private int PROXIMITY_RADIUS = 10000;
    com.mapbox.mapboxsdk.annotations.Marker mCurrLocationMarker;
    EditText search_Et;
    ImageView img_speech;
    String search_Str;
    Button search_Btn;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String[] title = new String[]{"police station", "hospital", "medical store", "bank", "atm", "hotel", "library", "garden", "railway station", "bus station", "fire station", "cafe", "petrol pump", "gym", "post office", "temple", "mosque", "church", "shopping malls", "movie theatre", "jewellery shop", "super market", "bakery", "book store", "spa", "school", "animal care", "toilets"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapbox_map_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_Btn = (Button) findViewById(R.id.go_Btn);
        search_Et = (EditText) findViewById(R.id.search_text);
        img_speech = (ImageView) findViewById(R.id.text_to_speech);

        mapView = (MapView) findViewById(R.id.map);
//        mapView.setStyleUrl("mapbox://styles/mapbox/satellite-streets-v9");
        mapView.setStyleUrl(Style.SATELLITE_STREETS);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search_Et.getText().toString().equalsIgnoreCase("")) {
                    mapboxMap.clear();
                    search_Str = search_Et.getText().toString();
                    String url = getUrl(latitude, longitude, search_Str);
                    int index = getIntent().getIntExtra("index", 0);
                    Object[] DataTransfer = new Object[4];
                    DataTransfer[0] = mapboxMap;
                    DataTransfer[1] = url;
                    DataTransfer[2] = search_Str;
                    for (int i = 0; i < title.length; i++) {
                        if (search_Str.trim().contains(title[i])) {
                            index = i;
                            break;
                        }
                    }
                    DataTransfer[3] = index;
                    Log.d("onClick", url);
                    search_Et.setText("");
                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapboxMapViewActivity.this);
                    getNearbyPlacesData.execute(DataTransfer);
                } else {

                    Toast.makeText(getApplicationContext(), "please enter text to search", Toast.LENGTH_LONG).show();
                }

            }
        });

        img_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_Et.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        enableLocation();

        String[] mapbox_Id = new String[]{"dev4.9pcz9q7b",
                "infoxyz.1muepkyd",
                "infoxyz.2esl0l2m",
                "infoxyz.drq0o8du",
                "infoxyz.0p4kwx5s",
                "infoxyz.3kbmrj3a",
                "infoxyz.9xgrwsk5",
                "infoxyz.d3y274cn",
                "infoxyz.dpszx4fj",
                "infoxyz.a0vaku1u",
                "infoxyz.5rd2668f",
                "infoxyz.8x89pdy2",
                "infoxyz.d3gmr7t4",
                "infoxyz.clb1tg5c",
                "infoxyz.6isxixob",
                "infoxyz.6gsldvka",
                "infoxyz.4tux9jsp"};
        String[] ward_no = new String[]{"27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43"};
        String[] ward_Layers = new String[]{"k1", "k2", "k3", "k4", "k5", "k6", "k7", "k8", "k9", "k10", "k11", "k12", "k13", "k14", "k15", "k16", "k17"};
        ;
        String mapboxId_Str = null, wardNo_Str = null, wardLayers_Id = null;
        for (int i = 0; i < mapbox_Id.length; i++) {

            mapboxId_Str = mapbox_Id[i];
            wardNo_Str = "wards" + ward_no[i];
            wardLayers_Id = ward_Layers[i];
            TileSet tileSet = new TileSet("2.2.0", "https://api.mapbox.com/v4/" + mapboxId_Str + "/{z}/{x}/{y}@2x.png?access_token=" + getString(R.string.mapbox_access_token));
            RasterSource rasterSource1 = new RasterSource(wardNo_Str, tileSet, 256);
            mapboxMap.addSource(rasterSource1);
            RasterLayer rasterLayer1 = new RasterLayer(wardLayers_Id, wardNo_Str);
            mapboxMap.addLayer(rasterLayer1);
        }
    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setLocationEngine(locationEngine);
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0));
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    public String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
//        googlePlacesUrl.append("&type=" + nearbyPlace.replaceAll("\\s+", ""));
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&keyword=" + nearbyPlace);
        googlePlacesUrl.append("&key=" + "AIzaSyA3fOMjmLMj_8-1ciHEYPjnz1NEipsIzHk");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        if (location != null && mCurrLocationMarker != null) {
            originLocation = location;
            setCameraPosition(location);
            mCurrLocationMarker.remove();
        }

/*
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
*/

//Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());

        Icon mIcon = IconFactory.getInstance(MapboxMapViewActivity.this).fromResource(R.drawable.smallcurrentlocation);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
        markerOptions.icon(mIcon);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource((int) BitmapDescriptorFactory.HUE_ORANGE))
        mCurrLocationMarker = mapboxMap.addMarker(markerOptions);

        String q_str = getIntent().getStringExtra("query");
        int index = getIntent().getIntExtra("index", 0);
        String url = getUrl(latitude, longitude, q_str.toLowerCase());
        Object[] DataTransfer = new Object[4];
        DataTransfer[0] = mapboxMap;
        DataTransfer[1] = url;
        DataTransfer[2] = q_str;
        DataTransfer[3] = index;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapboxMapViewActivity.this);
        getNearbyPlacesData.execute(DataTransfer);
    }

    @Override
    @SuppressWarnings("missingpermission")
    public void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }
}