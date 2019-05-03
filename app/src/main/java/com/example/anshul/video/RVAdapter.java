package com.example.anshul.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Anshul on 2/1/2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    public List<DTC_Videos> list;
    Context context;
    String mVideoUri;



    public  RVAdapter(Context context,List<DTC_Videos> list)
    {
        this.list = list;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView name,view_count;
        public ImageView image;
        public FrameLayout v1;
        public MyViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            name = view.findViewById(R.id.adap_name);
          //  view_count = view.findViewById(R.id.adap_views);
            image = view.findViewById(R.id.adap_img);
            v1 = view.findViewById(R.id.v1);
        }
        @Override
        public void onClick(View v) {

            int p = getAdapterPosition();
            // cont_rest c = this.c.get(p);
            DTC_Videos dtc_videos = list.get(p);
            String Uri = dtc_videos.getFile();
            Intent i = new Intent(context,Video.class);
            i.putExtra("uri", Uri);
            i.putExtra("id",dtc_videos.getId());
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

        mVideoUri = dtc_videos.getFile();

      //  BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), thumbnail);

        String thumb = dtc_videos.getThumb();
        Glide.with(context).load(thumb).into(holder.image);

        Log.i("length",""+dtc_videos.getLength());
        Log.i("comp",""+dtc_videos.getComplete());
        if(dtc_videos.getComplete()>0 && dtc_videos.getLength()>0)
        {

            int x = (int)((dtc_videos.getComplete()/dtc_videos.getLength())*100);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,x, context.getResources().getDisplayMetrics());
            holder.v1.setLayoutParams(new FrameLayout.LayoutParams(width,10));
            holder.v1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
