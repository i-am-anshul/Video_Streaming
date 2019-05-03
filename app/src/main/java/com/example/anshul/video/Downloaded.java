package com.example.anshul.video;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Downloaded extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RVAdapter2 mAdapter;
    public static List<DTC_Videos> list1 = new ArrayList<>();
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded);
        recyclerView = findViewById(R.id.rv2);
        mAdapter = new RVAdapter2(this, list1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        db = new DatabaseHandler(Downloaded.this);
        prepareData();


    }

    private void prepareData() {
        list1.clear();
        List<DTC_Videos> list2 = db.getAllVideos();

        for(int i = 0;i< list2.size();i++)
        {
            DTC_Videos d1 = list2.get(i);
            String key = d1.getName();
            key = key+".mp4";
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

            String key1 = pref.getString(key,null);
            if(key1 != null)
            {

                DTC_Videos d = new DTC_Videos();
                d.setLink(d1.getLink());
                d.setThumb(d1.getThumb());
                d.setFile(d1.getFile());
                d.setName(d1.getName());
                d.setCount(d1.getCount());
                d.setId(d1.getId());
                d.setComplete(d1.getComplete());
                d.setLength(d1.getLength());
                list1.add(d);
            }
        }

        mAdapter.notifyDataSetChanged();
    }
}
