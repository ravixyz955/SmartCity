package kakinada.smartcity.com.smartcitykakinada.googlemaps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.HashMap;
import java.util.List;

import kakinada.smartcity.com.smartcitykakinada.R;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url, queryType;
    int index;
    private MapboxMap mapboxMap;
    double latitude, longitude;
    Context context;
    //    MapViewActivity mapsActivity;
    MapboxMapViewActivity mapsActivity;

    private int[] markers;

    //    public GetNearbyPlacesData(MapViewActivity mapsActivity) {
    public GetNearbyPlacesData(MapboxMapViewActivity mapsActivity) {
        this.latitude = mapsActivity.latitude;
        this.longitude = mapsActivity.longitude;
        this.mapsActivity = mapsActivity;
        this.context = mapsActivity;
        markers = new int[]{R.drawable.markerpolicestation, R.drawable.markerhospital, R.drawable.markermedicalstore, R.drawable.markerbank, R.drawable.markeratm, R.drawable.markerhotel, R.drawable.markerlibrary, R.drawable.markergarden, R.drawable.markerrailwaystation, R.drawable.markerbusstation, R.drawable.markerfirestation, R.drawable.markercafe, R.drawable.markerpetrolbunk, R.drawable.markergym, R.drawable.markerpostoffice, R.drawable.markertemple, R.drawable.markermoque, R.drawable.markerchurch, R.drawable.markershooppingmall, R.drawable.markermovietheatre, R.drawable.markerjewelleryshop, R.drawable.markersupermarket, R.drawable.markerbakery, R.drawable.markerbookstore, R.drawable.markerspa, R.drawable.markerschool, R.drawable.markeranimalcare, R.drawable.markertoilet};
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
//            mMap = (GoogleMap) params[0];
            mapboxMap = (MapboxMap) params[0];
            url = (String) params[1];
            queryType = (String) params[2];
            index = (int) params[3];
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
//        Icon mIcon = IconFactory.getInstance(mapsActivity).fromResource(R.drawable.markerpolicestation);
        IconFactory iconFactory = IconFactory.getInstance(mapsActivity);

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
            Icon mIcon = iconFactory.fromResource(markers[index]);
//            double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, latLng) / 1000);
            double distance = Math.round(origin.distanceTo(latLng) / 1000);

            markerOptions.position(latLng);
            markerOptions.icon(mIcon);
//            markerOptions.title(placeName + ":" + vicinity);
            markerOptions.title(placeName + ":" + vicinity + ":" + distance + "km");
//            markerOptions.icon(BitmapDescriptorFactory.fromResource((int) BitmapDescriptorFactory.HUE_ORANGE));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap((((BitmapDrawable) (mapsActivity.getResources().getDrawable(R.drawable.markerpolicestation)))).getBitmap(), 60, 60, false)));
//            mMap.addMarker(markerOptions);
            mapboxMap.addMarker(markerOptions);
            /*PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions
                    .add(new LatLng(latitude, longitude))
                    .add(new LatLng(lat, lng))
                    .color(Color.BLUE);
            mMap.addPolyline(polygonOptions);*/
            //move map camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }
}
