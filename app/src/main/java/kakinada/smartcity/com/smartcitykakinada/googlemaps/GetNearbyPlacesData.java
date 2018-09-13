package kakinada.smartcity.com.smartcitykakinada.googlemaps;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.List;

import kakinada.smartcity.com.smartcitykakinada.R;

/**
 * Created by navneet on 23/7/16.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url, queryType;
    double latitude, longitude;
    MapViewActivity mapsActivity;

    public GetNearbyPlacesData(MapViewActivity mapsActivity) {
        this.latitude = mapsActivity.latitude;
        this.longitude = mapsActivity.longitude;
        this.mapsActivity = mapsActivity;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            queryType = (String) params[2];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng origin = new LatLng(latitude, longitude);
            LatLng latLng = new LatLng(lat, lng);
            double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, latLng) / 1000);

            /*// Getting URL to the Google Directions API, from here the route mapping is started
            String url = getDirectionsUrl(origin, latLng);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
*/
            markerOptions.position(latLng);
            markerOptions.title(placeName + ":" + vicinity + ":" + distance + "km");
//            markerOptions.icon(BitmapDescriptorFactory.fromResource((int) BitmapDescriptorFactory.HUE_ORANGE));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap((((BitmapDrawable) (mapsActivity.getResources().getDrawable(R.drawable.markerpolicestation)))).getBitmap(), 60, 60, false)));
            mMap.addMarker(markerOptions);
            /*PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions
                    .add(new LatLng(latitude, longitude))
                    .add(new LatLng(lat, lng))
                    .color(Color.BLUE);
            mMap.addPolyline(polygonOptions);*/
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
}
