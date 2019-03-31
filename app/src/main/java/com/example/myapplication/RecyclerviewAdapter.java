package com.example.myapplication;

 import android.content.Context;
 import android.content.Intent;
 import android.support.v7.widget.RecyclerView;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.TextView;
 import android.widget.Toast;
 import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewForUv";
    //vars
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescreption = new ArrayList<>();
    private Context mContext;

    public RecyclerviewAdapter(Context context,ArrayList<String> Price, ArrayList<String> Descreption) {
        this.mPrice = Price;
        this.mDescreption = Descreption;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.availablebookings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.rate.setText(mPrice.get(position));
        holder.descreption.setText(mDescreption.get(position));

        holder.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "You have Choosen"+ mPrice.get(position) + " Plan", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(view.getContext(),Payment.class);
                view.getContext().startActivity(i);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPrice.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView rate,descreption;
        Button purchase;

        public ViewHolder(View itemView) {
            super(itemView);

            rate        = itemView.findViewById(R.id.rupees);
            descreption = itemView.findViewById(R.id.address);
            purchase    = itemView.findViewById(R.id.buy);
        }
    }
}
