package com.example.anshul.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anshul on 2/1/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Videos";

    // Contacts table name
    private static final String TABLE_VIDEOS = "videos";

    // Contacts Table Columns names
    private static final String link = "link";
    private static final String thumb = "thumb";
    private static final String file = "file";
    private static final String name = "name";
    private static final String count = "count";
    private static final String id = "id";
    private static final String complete = "complete";
    private static final String length = "length";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VIDEOS_TABLE = "CREATE TABLE " + TABLE_VIDEOS + "( " + link + " TEXT, " + thumb + " TEXT, "
                + file + " TEXT, " + name + " TEXT, "
                + count + " INTEGER, " + id + " INTEGER PRIMARY KEY, "
                +complete + " FLOAT DEFAULT 0,"+ length + " INTEGER DEFAULT 0 "+")";
        db.execSQL(CREATE_VIDEOS_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);

        // Create tables again
        onCreate(db);
    }

    // Adding new dtc_videos
    public void addVideo(DTC_Videos dtc_videos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(link,dtc_videos.getLink());
        values.put(thumb,dtc_videos.getThumb());
        values.put(name, dtc_videos.getName());// DTC_Videos Name
        values.put(file, dtc_videos.getFile());
        values.put(count, dtc_videos.getCount());
        values.put(id, dtc_videos.getId());
        values.put(complete, dtc_videos.getComplete());
        values.put(length, dtc_videos.getLength());

        List<DTC_Videos> videosList = new ArrayList<DTC_Videos>();
        videosList = getAllVideos();
        int flag = 0;
        for(int i=0;i<videosList.size();i++)
        {
            DTC_Videos v1 = videosList.get(i);
            if(dtc_videos.getId() == v1.getId())
            {
                flag =v1.getId();
            }

        }

        // Inserting Row
        if(flag == 0) {
            db.insert(TABLE_VIDEOS, null, values);
        }
     /*   else
        {
          *//*  DTC_Videos d1 =  getVideo(flag);

            int c = d1.getCount();
            int d = v1.getCount();
            int e = d-c;
            d1.setCount(c+e);
            updateCount(d1);*//*

        }*/

            db.close(); // Closing database connection
    }

    // Getting single dtc_videos
    DTC_Videos getVideo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VIDEOS, new String[] { link,thumb,file,
                        name, count,DatabaseHandler.id,complete,length },  DatabaseHandler.id+ "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DTC_Videos dtc_videos = new DTC_Videos(cursor.getString(0),cursor.getString(1),
                cursor.getString(2),cursor.getString(3),Integer.parseInt( cursor.getString(4)),
                Integer.parseInt( cursor.getString(5)),Float.parseFloat(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)));
        // return dtc_videos
        return dtc_videos;
    }

    // Getting All Contacts
    public List<DTC_Videos> getAllVideos() {
        List<DTC_Videos> videosList = new ArrayList<DTC_Videos>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DTC_Videos dtc_videos = new DTC_Videos();
                dtc_videos.setLink(cursor.getString(0));
                dtc_videos.setThumb(cursor.getString(1));
                dtc_videos.setFile(cursor.getString(2));
                dtc_videos.setName(cursor.getString(3));
                dtc_videos.setCount(Integer.parseInt(cursor.getString(4)));
                dtc_videos.setId(Integer.parseInt(cursor.getString(5)));
                dtc_videos.setComplete(Float.parseFloat(cursor.getString(6)));
                dtc_videos.setLength(Integer.parseInt(cursor.getString(7)));
                
                // Adding dtc_videos to list
                videosList.add(dtc_videos);
            } while (cursor.moveToNext());
        }

        // return dtc_videos list
        return videosList;
    }

    // Updating single dtc_videos
    public int updateCount(DTC_Videos dtc_videos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(link, dtc_videos.getLink());
        values.put(thumb, dtc_videos.getThumb());// DTC_Videos Name
        values.put(name, dtc_videos.getName());// DTC_Videos Name
        values.put(file, dtc_videos.getFile());
        values.put(count, dtc_videos.getCount());
        values.put(id, dtc_videos.getId());
        values.put(complete, dtc_videos.getComplete());
        values.put(length, dtc_videos.getLength());

        // updating row
        return db.update(TABLE_VIDEOS, values, DatabaseHandler.id + " = ?",
                new String[] { String.valueOf(dtc_videos.getId()) });
    }

    // Deleting single dtc_videos
    public void deleteContact(DTC_Videos dtc_videos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEOS, DatabaseHandler.id + " = ?",
                new String[] { String.valueOf(dtc_videos.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getVideosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VIDEOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
