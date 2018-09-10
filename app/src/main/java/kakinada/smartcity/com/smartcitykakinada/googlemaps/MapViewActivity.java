package kakinada.smartcity.com.smartcitykakinada.googlemaps;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import kakinada.smartcity.com.smartcitykakinada.R;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    String lat,lang,plname,latLngString,current_lat,current_lang,s_Query;
    LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         lat=getIntent().getExtras().getString("lat");
         lang=getIntent().getExtras().getString("lang");
         plname=getIntent().getExtras().getString("title");

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        Polyline line;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(DelatilsActivity.cur_Lat, DelatilsActivity.cur_Lang))
                .title("your location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lang)))
                .title(plname)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
         line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(DelatilsActivity.cur_Lat, DelatilsActivity.cur_Lang), new LatLng(Double.parseDouble(lat), Double.parseDouble(lang)))
                .width(5)
                .color(Color.BLUE));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lang)), 12));

    }
}
