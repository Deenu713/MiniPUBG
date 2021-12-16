package com.autohost.genesis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.autohost.genesis.fragments.CoreFragment;
import com.autohost.genesis.tools.GenesisTools;
import com.autohost.genesis.tools.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import stream.customalert.CustomAlertDialogue;

public class MainActivity extends AppCompatActivity {

    private int public_key,private_key;
    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);


        final CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle("Welcome to Genesis")
                .setMessage("Connecting server...")
                .setMessageColor(R.color.colorGreen)
                .setTitleColor(R.color.colorBlue)
                .setCancelable(false)
                .setAutoHide(true)
                .setTimeToHide(3000)
                .setDecorView(getWindow().getDecorView())
                .build();
        alert.show();

        Utils.getRoot();
        pullimei();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new playOn(R.raw.gen_welcome_login).execute();

            }
        },1800);

        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            Toast.makeText(getApplicationContext(),"Please Allow Overlay Permission\nRestart Genesis",Toast.LENGTH_LONG).show();
            startActivityForResult(intent, 1234);
        }


    }


    public  void loadServer(){

        CoreFragment coreFragment = new CoreFragment();

        if(public_key==private_key) {

            GenesisTools.VIPDRM();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, coreFragment).commit();
        }
    }


    public void getPrivateKey(int key){
        private_key = key;
    }

    public void getPublicKey(int key){
        public_key = key;
    }










    public void pullimei(){

     //   String ext = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
    //    Toast.makeText(getApplicationContext(),ext,Toast.LENGTH_LONG).show();



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("ip=$(service call iphonesubinfo 1 | grep -o \"[0-9a-f]\\{8\\} \" | tail -n+3 | while read a; do echo -n \"\\u${a:4:4}\\u${a:0:4}\"; done)\n echo $ip > /sdcard/z.ini \n"); os.flush();

            os.writeBytes("exit\n"); os.flush();




        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.getQDevice(getApplicationContext());

    }


    public class  playOn extends AsyncTask<String,Integer,String> {

        int audio;

        playOn(int audio){
            this.audio = audio;
        }


        @Override
        protected String doInBackground(String... strings) {



            return null;
        }
    }



}
