package kakinada.smartcity.com.smartcitykakinada.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kakinada.smartcity.com.smartcitykakinada.R;
import kakinada.smartcity.com.smartcitykakinada.googlemaps.DelatilsActivity;


public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    TextView name, desc,detail_Text;
    ImageView img;
    LinearLayout image;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    String listType = null;
    Context ctx;
    int size;
    String send_query;
    Integer[] mThumbIds = {R.drawable.policestation, R.drawable.hospital, R.drawable.medicalstore, R.drawable.bankbuilding, R.drawable.atm, R.drawable.hotel, R.drawable.library, R.drawable.garden, R.drawable.railwaystation, R.drawable.busstation, R.drawable.firestation, R.drawable.cafe, R.drawable.petrolpump, R.drawable.gym, R.drawable.postoffice, R.drawable.temple, R.drawable.mosque, R.drawable.church, R.drawable.shoppingmall, R.drawable.theater, R.drawable.jeweleryshop, R.drawable.supermarket, R.drawable.bakeryshop, R.drawable.bookstore, R.drawable.spa, R.drawable.school, R.drawable.animalcare};

    public CustomAdapter(Context context, JSONArray jsonArray, String listType, Context applicationContext) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.listType = listType;
        layoutInflater = LayoutInflater.from(context);
        ctx = applicationContext;
        Log.d("TAG", "CustomAdapter: " + jsonArray.length());
    }

    @Override
    public int getCount() {
        Log.d("TAG", "getCount: " + jsonArray.length());
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.gridview_item, null);
        name = (TextView) view.findViewById(R.id.name);
        desc = (TextView) view.findViewById(R.id.desc);
        detail_Text = (TextView) view.findViewById(R.id.detail_Text);

//        image = (LinearLayout) view.findViewById(R.id.image);
        img = (ImageView) view.findViewById(R.id.list_img);
        //view.setOnClickListener(this);

        if (listType.equalsIgnoreCase("dashboard")) {
            Log.d("CUSTOMADAPTERIF", "getView: customadapter");

            img.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            detail_Text.setVisibility(View.GONE);
            try {
                jsonObject = jsonArray.getJSONObject(i);

                name.setText(jsonObject.getString("name"));
                desc.setText(jsonObject.getString("desc"));

//                image.setBackgroundResource(R.drawable.ic_launcher_background);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("LISTRESPONSE", "getView: customadapter");
/*
            try {
                jsonObject = jsonArray.getJSONObject(i);
                URL url = new URL(jsonObject.getString("icon"));
                Glide.with(context)
                        .load(url)
                        .into(img);
                name.setText(jsonObject.getString("text"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
*/
            try {
                jsonObject = jsonArray.getJSONObject(i);
                img.setBackgroundResource(mThumbIds[i]);
                detail_Text.setVisibility(View.VISIBLE);
                name.setVisibility(View.INVISIBLE);
                desc.setVisibility(View.INVISIBLE);
                detail_Text.setText(jsonObject.getString("text"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }


}