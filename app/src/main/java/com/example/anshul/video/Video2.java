package com.example.anshul.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Video2 extends AppCompatActivity {
    public String s, dw, name, thumb;
    VideoView v1;

    ProgressBar spinnerView;
    String id;
    DatabaseHandler d1;
    DTC_Videos d;

    String finalUrl;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video2);

        v1 = findViewById(R.id.vv2);
        spinnerView = findViewById(R.id.sp2);

        Intent i = getIntent();

        id = i.getExtras().getString("name");
        MediaController mediaController = new MediaController(Video2.this);

        mediaController.setAnchorView(v1);
        v1.setMediaController(mediaController);

        String rootDir = Environment.getExternalStorageDirectory()
                + File.separator + "Video/myapp";
        File rootFile = new File(rootDir);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = id+".mp4";
        String k = pref.getString(id, null);


        byte[] array = Base64.decode(k, Base64.NO_WRAP);
        SecretKey key = new SecretKeySpec(array, "AES");


        try {
            //decryption and saving video
            Cipher decipher = Cipher.getInstance("AES");
            decipher.init(Cipher.DECRYPT_MODE, key);
            File rootFile1 = new File(rootDir);
            rootFile1.mkdir();
            file = new File(rootFile1,
                    "decrypted.mp4");
            FileOutputStream f1 = new FileOutputStream(file);
            FileInputStream fi1 = new FileInputStream(new File(rootFile1,
                    id));
            CipherOutputStream cos = new CipherOutputStream(f1, decipher);
            byte[] buffer = new byte[1024 * 7];
            int len1 = 0;
            while ((len1 = fi1.read(buffer)) >= 0) {
                cos.write(buffer, 0, len1);
                cos.flush();
            }
            play(rootDir);
            cos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        v1.canPause();
        v1.canSeekBackward();
        v1.canSeekForward();




        MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    spinnerView.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    spinnerView.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    spinnerView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
        v1.setOnInfoListener(onInfoToPlayStateListener);

    }

    void play( String rootDir)
    {

        String path = rootDir + "/decrypted.mp4";

        v1.setVideoPath(path);
        v1.start();
    }

    @Override
    protected void onPause() {
        super.onPause();



        file.delete();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

        file.delete();
    }
}
