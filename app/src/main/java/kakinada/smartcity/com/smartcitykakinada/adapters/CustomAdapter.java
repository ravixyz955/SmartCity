package kakinada.smartcity.com.smartcitykakinada.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import kakinada.smartcity.com.smartcitykakinada.R;


public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    TextView name, desc;
    ImageView img;
    LinearLayout image;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    String listType = null;
    int size;

    public CustomAdapter(Context context, JSONArray jsonArray, String listType) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.listType = listType;
        layoutInflater = LayoutInflater.from(context);
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
//        image = (LinearLayout) view.findViewById(R.id.image);
        img = (ImageView) view.findViewById(R.id.list_img);

        if (listType.equalsIgnoreCase("dashboard")) {
            Log.d("CUSTOMADAPTERIF", "getView: customadapter");

            img.setVisibility(View.GONE);
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
                img.setBackgroundResource(R.drawable.city_council);
                name.setText(jsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return view;
    }
}