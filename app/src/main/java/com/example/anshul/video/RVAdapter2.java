package com.example.anshul.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Anshul on 2/1/2018.
 */

public class RVAdapter2 extends RecyclerView.Adapter<RVAdapter2.MyViewHolder> {

    public List<DTC_Videos> list;
    Context context;
    String mVideoUri;



    public RVAdapter2(Context context, List<DTC_Videos> list)
    {
        this.list = list;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView name,view_count;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            name = view.findViewById(R.id.adap_name);
          //  view_count = view.findViewById(R.id.adap_views);
            image = view.findViewById(R.id.adap_img);

        }
        @Override
        public void onClick(View v) {

            int p = getAdapterPosition();
            // cont_rest c = this.c.get(p);
            DTC_Videos dtc_videos = list.get(p);

            Intent i = new Intent(context,Video2.class);

            i.putExtra("name",dtc_videos.getName());
            context.startActivity(i);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rvadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DTC_Videos dtc_videos = list.get(position);
      //  Toast.makeText(context,dtc_videos.getName(), Toast.LENGTH_SHORT).show();
        holder.name.setText(dtc_videos.getName());
    //    holder.view_count.setText(""+dtc_videos.getCount());


      //  BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), thumbnail);

        String thumb = dtc_videos.getThumb();
        Glide.with(context).load(thumb).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
