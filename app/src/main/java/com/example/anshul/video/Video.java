package com.example.anshul.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
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
import javax.net.ssl.HttpsURLConnection;

public class Video extends AppCompatActivity {
    public String s, dw, name,thumb;
    VideoView v1;
    Button b1;
    ProgressBar spinnerView;
    int id, p;
    DatabaseHandler d1;
    DTC_Videos d;

    String finalUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        v1 = findViewById(R.id.vv1);
        spinnerView = findViewById(R.id.sp1);

        Intent i = getIntent();
        s = i.getExtras().getString("uri");
        id = i.getExtras().getInt("id");
        MediaController mediaController = new MediaController(Video.this);

        mediaController.setAnchorView(v1);
        v1.setMediaController(mediaController);

        v1.setVideoURI(Uri.parse(s));
        v1.requestFocus();
        d1 = new DatabaseHandler(this);
        d = d1.getVideo(id);
        float x = d.getComplete();
        dw = d.getLink();
        name = d.getName();
        thumb = d.getThumb();


        v1.seekTo((int) x);

        v1.start();


        v1.canPause();
        v1.canSeekBackward();
        v1.canSeekForward();

        v1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                d = d1.getVideo(id);
                p = v1.getDuration();
                d.setLength(p);
                d1.updateCount(d);

            }


        });
        b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v1.pause();
                ProgressBack PB = new ProgressBack();
                PB.execute("");

                Toast.makeText(Video.this, finalUrl, Toast.LENGTH_SHORT).show();

            }
        });


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

    class ProgressBack extends AsyncTask<String, String, String> {
        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
            PD = ProgressDialog.show(Video.this, null, "Downloading! Please Wait ...", true);
            PD.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile(dw, name,thumb);
            onPostExecute();
            return "";
        }

        protected void onPostExecute() {
            PD.dismiss();


        }

    }

    private void downloadFile(String fileURL, String fileName,String thumb) {
        try {

            //making dir
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video/myapp";
            File rootFile = new File(rootDir);
            fileName = fileName + ".mp4";
            //String filename1 = rootDir + File.separator + "v1.mp4";
            finalUrl = rootFile + File.separator + fileName;
            rootFile.mkdir();


            //making dir

            //making conn
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            //making conn






            //encryption and saving video
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));

            Cipher encipher = Cipher.getInstance("AES");

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //byte key[] = {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
            SecretKey skey = kgen.generateKey();
            //Lgo
            encipher.init(Cipher.ENCRYPT_MODE, skey);
            InputStream is = c.getInputStream();
            CipherInputStream cis = new CipherInputStream(is, encipher);
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = cis.read(buffer)) >= 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
            //encryption and saving video


            //saving key for further use
            byte[] enc = skey.getEncoded();
            String encs = Base64.encodeToString(enc, Base64.DEFAULT);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(fileName,encs);
            editor.commit();




            String k = pref.getString(fileName, null);
            byte[] array = Base64.decode(k, Base64.DEFAULT);
            SecretKey key = new SecretKeySpec(array, "AES");





            //decryption and saving video
            /*try {

                //decryption and saving video
                Cipher decipher = Cipher.getInstance("AES");
                decipher.init(Cipher.DECRYPT_MODE, skey);
                File rootFile1 = new File(rootDir);
                rootFile1.mkdir();
                File file = new File(rootFile1,
                        "decrypted.mp4");
                FileOutputStream f1 = new FileOutputStream(file);
                FileInputStream fi1 = new FileInputStream(new File(rootFile,
                        fileName));
                CipherOutputStream cos = new CipherOutputStream(f1, decipher);
                 len1 = 0;
                while ((len1 = fi1.read()) != -1) {
                    cos.write(len1);
                    cos.flush();
                }
                cos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }*/


        } catch (IOException e) {
            Log.d("Error....", e.toString());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        int k = v1.getCurrentPosition();


        DTC_Videos d = d1.getVideo(id);
        d.setComplete(k);
        d1.updateCount(d);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DTC_Videos d = d1.getVideo(id);
        float x = d.getComplete();

        v1.seekTo((int) x);
        v1.start();
    }


}
