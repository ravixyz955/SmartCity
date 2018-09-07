package kakinada.smartcity.com.smartcitykakinada;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIService;
import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIUtils;
import kakinada.smartcity.com.smartcitykakinada.Utils.DialogsUtils;
import kakinada.smartcity.com.smartcitykakinada.adapters.CustomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {
    private APIService apiService = null;
    private GridView gridView = null;
    private JSONArray jsonElements = null;
    ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        apiService = APIUtils.getAPIService();
        gridView = (GridView) findViewById(R.id.simpleGridView);
        myDialog= DialogsUtils.showProgressDialog(this,"Loding");
        myDialog.show();

        apiService.dashboard().enqueue(new Callback<Object>() {
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
                Toast.makeText(MainActivity1.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    performAction(MainActivity1.this, action, key);
//                    Toast.makeText(MainActivity.this, "" + jsonObject.getString("action") + "    " + jsonObject.getString("key"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void performAction(MainActivity1 context, String action, String key) {
        Intent i = new Intent(context, SwitchToActionActivity.class);
        i.putExtra("action", action);
        i.putExtra("key", key);
        context.startActivity(i);
    }
}
