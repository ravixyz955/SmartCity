package kakinada.smartcity.com.smartcitykakinada;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIService;
import kakinada.smartcity.com.smartcitykakinada.NetworkCalls.APIUtils;
import kakinada.smartcity.com.smartcitykakinada.adapters.CustomListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwitchToActionActivity extends AppCompatActivity {

    Intent i = null;
    String action, key;
    private APIService apiService = null;
    private RecyclerView recyclerView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_to_action);

        apiService = APIUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        i = getIntent();
        action = i.getStringExtra("action");
        key = i.getStringExtra("key");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        //Toast.makeText(this, "   " + action + "   " + key, Toast.LENGTH_SHORT).show();

        switch (action) {
            case "detail-list":
                apiService.discoverList().enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.body() != null) {
                            String json_string = new Gson().toJson(response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(json_string);
                                JSONArray jsonArray = jsonObject.getJSONArray("discoverlist");
                                CustomListAdapter customAdapter = new CustomListAdapter(getApplicationContext(), jsonArray, "discoverlist");
                                recyclerView.setAdapter(customAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(SwitchToActionActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "smartcity-list":
                apiService.smartCityList().enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.body() != null) {
                            String json_string = new Gson().toJson(response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(json_string);
                                JSONArray jsonArray = jsonObject.getJSONArray("smartcitylist");
                                CustomListAdapter customAdapter = new CustomListAdapter(getApplicationContext(), jsonArray, "smartcitylist");
                                recyclerView.setAdapter(customAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(SwitchToActionActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
                case "govtservice-list":
                apiService.govtserviceList().enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body() != null) {
                String json_string = new Gson().toJson(response.body());
                try {
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("govtservicelist");
                CustomListAdapter customAdapter = new CustomListAdapter(getApplicationContext(), jsonArray, "govtservicelist");
                recyclerView.setAdapter(customAdapter);
               } catch (JSONException e) {
                e.printStackTrace();
            }
         }
        }

@Override
public void onFailure(Call<Object> call, Throwable t) {
Toast.makeText(SwitchToActionActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
}
});
break;

        }
    }
}
