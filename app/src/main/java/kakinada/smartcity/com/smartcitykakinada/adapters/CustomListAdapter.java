package kakinada.smartcity.com.smartcitykakinada.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import kakinada.smartcity.com.smartcitykakinada.R;


public class CustomListAdapter extends RecyclerView.Adapter {
    private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
    private ImageView img;
    private TextView txt;

    public CustomListAdapter(Context applicationContext, JSONArray jsonArray, String discoverlist) {
        this.jsonArray = jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_list, parent, false);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        String icon;
        try {
            jsonObject = jsonArray.getJSONObject(position);
            holder1.txt.setText(jsonObject.getString("text"));
             icon=jsonObject.getString("icon").toString();
            if(icon.equalsIgnoreCase("http://url-to-icon")){
            holder1.image.setImageResource(R.drawable.events);

                }
                else{
                    try {
                        java.net.URL url = new java.net.URL(icon);
                        android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        holder1.image.setImageBitmap(bmp);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
}

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        Log.d("COUNT", "getItemCount: " + jsonArray.length());
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

// get the reference of item view's
            image = (ImageView) itemView.findViewById(R.id.discoverlist_img);
            txt = (TextView) itemView.findViewById(R.id.discoverlist_txt);
        }
    }
}