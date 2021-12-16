package com.autohost.genesis.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.autohost.genesis.ClassicService;
import com.autohost.genesis.MC;
import com.autohost.genesis.MainActivity;
import com.autohost.genesis.R;
import com.autohost.genesis.Server;
import com.autohost.genesis.tools.Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoFragment extends Fragment {

    private MC mc = new MC();
    private int rx;
    private TextView status;
    private String imei;
    private boolean isRoot = true;
   // private View ma

    public NoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainview =  inflater.inflate(R.layout.fragment_no, container, false);

        final EditText keyinput = mainview.findViewById(R.id.editText_key);
        final Button submit = mainview.findViewById(R.id.button_enter);
        status = mainview.findViewById(R.id.textView_status);
        final CheckBox vmmode = mainview.findViewById(R.id.vmmode);
        final CheckBox vmosmode = mainview.findViewById(R.id.vmosmode);
        final RadioGroup gameTypeSelection = mainview.findViewById(R.id.server_grp);



        rx = new Random().nextInt(9999999);
        String prekey = Utils.ReadEternal(getActivity(),"key");
        keyinput.setText(prekey);

        ((MainActivity)getActivity()).getPublicKey(rx);



       // getActivity().startService(new Intent(getActivity(), ClassicService.class));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT ==30){
                    getActivity().finish();
                    Toast.makeText(getContext(),"Android 11 Not Supported in MiniCheat . Buy Genesis for Android 11",Toast.LENGTH_SHORT).show();
                } else {

                    if(vmmode.isChecked()){
                        Utils.WriteEternal(getContext(),"root","0");
                        Utils.WriteEternal(getContext(),"vmos","0");

                        isRoot = false;
                    } else {
                        Utils.WriteEternal(getContext(),"root","1");
                        Utils.WriteEternal(getContext(),"vmos","0");

                        isRoot = true;
                    }
                    if(vmosmode.isChecked()){
                        Utils.WriteEternal(getContext(),"root","1");
                        Utils.WriteEternal(getContext(),"vmos","1");
                        isRoot = true;

                    }
                    int serverId = gameTypeSelection.getCheckedRadioButtonId();
                    if(serverId==R.id.rd_global){
                        Utils.WriteEternal(getContext(),"game.ini","com.tencent.ig");
                    } else if(serverId==R.id.rd_korea){
                        Utils.WriteEternal(getContext(),"game.ini","com.pubg.krmobile");
                    }


                    getimei();
                }

                status.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                keyinput.setVisibility(View.INVISIBLE);
                vmmode.setVisibility(View.INVISIBLE);
                vmosmode.setVisibility(View.INVISIBLE);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("device",imei);
                hashMap.put("user",keyinput.getText().toString());
                hashMap.put("salt",""+rx);

                String global_key = keyinput.getText().toString();
                Utils.WriteEternal(getActivity(),"key",global_key);
                Utils.WriteEternal(getActivity(),"key.txt",global_key);


                new Auth(hashMap).execute();
            }
        });



        return mainview;
    }





    private class Auth extends AsyncTask<String,Integer,String> {

        HashMap<String,String> hashMap;
        Auth(HashMap<String,String> hashMap){
            this.hashMap = hashMap;
        }
        @Override
        protected String doInBackground(String... strings) {
            String url = "http://autohost.tech/minipubg/07/login.memory.php";
            return Server.performPostCall(url,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(!s.isEmpty()){

                String[] svrData = s.split(",");
                int rxd = Integer.parseInt(svrData[0]);
                int rcode = Integer.parseInt(svrData[1]);
                String svrmsg = svrData[2];

                if(rx!=rxd){
                    getActivity().finish();
                } else {
                    ((MainActivity)getActivity()).getPrivateKey(rxd);

                    if(rcode==0){
                        final MediaPlayer sound_welcome_login = MediaPlayer.create(getContext(), R.raw.gen_deny);
                        sound_welcome_login.start();
                        MessageBoard(svrmsg);
                    } else if(rcode==1){
                      //  final MediaPlayer sound_welcome_login = MediaPlayer.create(getContext(), R.raw.gen_welcome);
                        //sound_welcome_login.start();
                        MessageBoard(svrmsg);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                status.setVisibility(View.GONE);
                                postLogin();

                            }
                        },4000);
                    }
                }
            }



        }
    }


    private void postLogin(){

        ((MainActivity)getActivity()).loadServer();

    }


    private void alert(String msg){
           new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle("Alert")
                .setMessage(msg)
                .setPositiveButton("OK",null).create().show();
    }

    private void MessageBoard(String msg){



      status.setText(msg);
    }

    void getimei(){


        String imei_input = "";

        if(isRoot){
            imei_input  = Utils.readIntegerFromFile("/storage/emulated/0/","z.ini");
        } else {
            imei_input = Utils.getVMDevice(getContext());
        }

        imei_input.replace(" ","").replace("\n","").trim();


        imei = imei_input;

    }

}
