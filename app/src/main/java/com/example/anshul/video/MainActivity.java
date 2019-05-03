package com.example.anshul.video;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RVAdapter mAdapter;
    public static List<DTC_Videos> list1 = new ArrayList<>();
    DatabaseHandler db;

    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHandler(MainActivity.this);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        recyclerView = findViewById(R.id.rv1);
        mAdapter = new RVAdapter(this, list1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();

        b1 = findViewById(R.id.b121);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Downloaded.class);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareData();
    }

    private void prepareData() {
        list1.clear();

       if (isInternetOn()) {


            AndroidNetworking.initialize(getApplicationContext());
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
            AndroidNetworking.setParserFactory(new JacksonParserFactory());
            AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

            AndroidNetworking.get("http://video-tuts.getsandbox.com/videos")
                    .setPriority(Priority.MEDIUM)
                    .setPriority(Priority.LOW)
                    .setTag("tag")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {


                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    String link = obj.getString("download");
                                    String thumb = obj.getString("thumbnail");
                                    String file = obj.getString("file");
                                    String name = obj.getString("name");
                                    int count = obj.getInt("count");
                                    int id = obj.getInt("id");
                                    DTC_Videos d = new DTC_Videos();
                                    d.setLink(link);
                                    d.setThumb(thumb);
                                    d.setFile(file);
                                    d.setName(name);
                                    d.setCount(count);
                                    d.setId(id);



                                    db.addVideo(d);
                                }
                                add_list();
                                mAdapter.notifyDataSetChanged();
                          /*  mAdapter = new RVAdapter(getBaseContext(), list1);
                            recyclerView.setAdapter(mAdapter);
*/
                            } catch (Exception e) {
                                Log.e("Json Object", "Json object");
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Toast.makeText(getApplicationContext(), "Error Occured While Fetching", Toast.LENGTH_SHORT).show();
                        }


                    });
        } else {

           add_list();
        }

      /*  JSONArray cast = jsonResponse.getJSONArray("abridged_cast");
        for (int i=0; i<cast.length(); i++) {
            JSONObject actor = cast.getJSONObject(i);
            String name = actor.getString("name");
            allNames.add(name);
        }*/
    }

    void add_list()
    {
        list1.clear();
        List<DTC_Videos> list2 = db.getAllVideos();

        for(int i = 0;i< list2.size();i++)
        {
            DTC_Videos d1 = list2.get(i);
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

        mAdapter.notifyDataSetChanged();
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            Toast.makeText(getBaseContext(), " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(getBaseContext(), " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
