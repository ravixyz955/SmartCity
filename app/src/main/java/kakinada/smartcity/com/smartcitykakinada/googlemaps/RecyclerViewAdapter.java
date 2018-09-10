package kakinada.smartcity.com.smartcitykakinada.googlemaps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kakinada.smartcity.com.smartcitykakinada.R;


/**
 * Created by anupamchugh on 01/03/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {


    private List<PlacesPOJO.CustomA> stLstStores;
    private List<StoreModel> models;
    String pl_name;
    Context ctx;

    public RecyclerViewAdapter(List<PlacesPOJO.CustomA> stores, List<StoreModel> storeModels, Context applicationContext) {

        stLstStores = stores;
        models = storeModels;
        ctx=applicationContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(stLstStores!=null){
            holder.setData(stLstStores.get(holder.getAdapterPosition()), holder, models.get(holder.getAdapterPosition()));

        }
    }


    @Override
    public int getItemCount() {
        //return Math.min(5, stLstStores.size());
        return (stLstStores.size());
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtStoreName;
        TextView txtStoreAddr;
        TextView txtStoreDist;
        StoreModel model;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtStoreDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtStoreAddr);
           // itemView.setOnClickListener(this);


        }


        public void setData(PlacesPOJO.CustomA info, MyViewHolder holder, StoreModel storeModel) {


            this.model = storeModel;

            holder.txtStoreDist.setText(model.distance + "\n" + model.duration);
            holder.txtStoreName.setText(info.name);
            holder.txtStoreAddr.setText(info.vicinity);
            pl_name=info.name.toString();


        }


        @Override
        public void onClick(View view) {
            Toast.makeText(ctx,"ddd"+txtStoreDist.getText().toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ctx, MapViewActivity.class);
            intent.putExtra("lat",model.distance.toString());
            intent.putExtra("lang",model.duration.toString());
            intent.putExtra("title",pl_name);

            ctx.startActivity(intent);

        }
    }
}
