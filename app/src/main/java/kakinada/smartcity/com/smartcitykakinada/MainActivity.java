package kakinada.smartcity.com.smartcitykakinada;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import android.widget.AdapterView;
import android.widget.Toast;
import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIService;
import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIUtils;
import kakinada.smartcity.com.smartcitykakinada.Utils.DialogsUtils;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import kakinada.smartcity.com.smartcitykakinada.adapters.CustomAdapter;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CarouselView carouselView;
    private APIService apiService = null;
    private GridView gridView = null;
    private JSONArray jsonElements = null;
    ProgressDialog myDialog;
    int[] sampleImages = {R.drawable.smart, R.drawable.k1, R.drawable.kakin, R.drawable.kakinada, R.drawable.profile};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        apiService = APIUtils.getAPIService();
        gridView = (GridView) findViewById(R.id.simpleGridView);
        myDialog= DialogsUtils.showProgressDialog(this,"Loding");
        myDialog.show();

        apiService.dashboard().enqueue(new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                myDialog.dismiss();
                if (response.body() != null) {
                    String json_string = new Gson().toJson(response.body());
                    try {
                        jsonElements = new JSONArray(json_string);
                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), jsonElements, "");
                        gridView.setAdapter(customAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                myDialog.dismiss();
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonElements.getJSONObject(i).getString("link"));
                    String action, key;
                    action = jsonObject.getString("action");
                    key = jsonObject.getString("key");
                    performAction(MainActivity.this, action, key);
//                    Toast.makeText(MainActivity.this, "" + jsonObject.getString("action") + "    " + jsonObject.getString("key"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


        public static void performAction(MainActivity context, String action, String key) {
        android.content.Intent i = new android.content.Intent(context, SwitchToActionActivity.class);
        i.putExtra("action", action);
        i.putExtra("key", key);
        context.startActivity(i);
    }
}
