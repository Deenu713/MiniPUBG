package com.autohost.genesis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.autohost.genesis.tools.GenesisTools;
import com.autohost.genesis.tools.Utils;
import com.google.android.material.tabs.TabLayout;

public class GenesisService extends Service {


    private View mainview;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private LinearLayout vtb1,vtb2,vtb3,vtb4,vtb5;
    private boolean isWindowShowing = true;
    private Handler mHandler = new Handler();
    private CheckBox flsBox;
    private FrameLayout mFrameLayout;
    private String svr_type;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mainview = inflater.inflate(R.layout.injector,null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity =   Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = 0;



        mFrameLayout = new FrameLayout(this);
        windowManager.addView(mFrameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.injector, mFrameLayout);


        vtb1 = mFrameLayout.findViewById(R.id.vtb1);
        vtb2 = mFrameLayout.findViewById(R.id.vtb2);
        vtb3 = mFrameLayout.findViewById(R.id.vtb3);
        vtb4 = mFrameLayout.findViewById(R.id.vtb4);
        vtb5 = mFrameLayout.findViewById(R.id.vtb5);

        Switch server_conn = mFrameLayout.findViewById(R.id.switch_connect);
        Switch lessrecoil = mFrameLayout.findViewById(R.id.switch_lr);
        Switch instashot = mFrameLayout.findViewById(R.id.switch_instashot);
        Switch aimbot = mFrameLayout.findViewById(R.id.switch_aimbot);
        Switch aimhead = mFrameLayout.findViewById(R.id.switch_aimhead);
        Switch magic = mFrameLayout.findViewById(R.id.switch_magic);
        final Switch flash = mFrameLayout.findViewById(R.id.switch_flash);

        Switch wideview = mFrameLayout.findViewById(R.id.switch_wideview);
        Switch blackbody = mFrameLayout.findViewById(R.id.switch_blackbody);
        Switch nofog = mFrameLayout.findViewById(R.id.switch_removeFog);
        Switch blacksky = mFrameLayout.findViewById(R.id.switch_blacksky);
        Switch rainbowsky = mFrameLayout.findViewById(R.id.switch_rainbowsky);
        Switch fastpara  = mFrameLayout.findViewById(R.id.switch_fastpara);
        Switch watercity = mFrameLayout.findViewById(R.id.switch_watercity);
        Switch antenna = mFrameLayout.findViewById(R.id.switch_antennapro);
        Switch wallhack = mFrameLayout.findViewById(R.id.switch_wallhack);
        Switch flyuaz = mFrameLayout.findViewById(R.id.switch_flyuaz);
        SeekBar seekFly = mFrameLayout.findViewById(R.id.seek_fly);
        SeekBar seekSpeed = mFrameLayout.findViewById(R.id.seekBar_speed);
        SeekBar seekRecoil = mFrameLayout.findViewById(R.id.seekBar_recoil);
        SeekBar seekHeadshot = mFrameLayout.findViewById(R.id.seekBar_headshot);
        Switch nofoot = mFrameLayout.findViewById(R.id.switch_nofoot);
        Switch memoryBypass = mFrameLayout.findViewById(R.id.switch_memorylobby);
        Switch memoryCF = mFrameLayout.findViewById(R.id.switch_memoryCF);
        final LinearLayout flsct = mFrameLayout.findViewById(R.id.flysection);
        TextView svrid = mFrameLayout.findViewById(R.id.svrid);
        CardView section_genesis = mFrameLayout.findViewById(R.id.section_genesis);
        CardView section_mem = mFrameLayout.findViewById(R.id.section_memory);


        flsBox  = mFrameLayout.findViewById(R.id.checkBox_flashhandler);


        hideallTabs(); vtb1.setVisibility(View.VISIBLE);




        final TabLayout tabLayout =  mFrameLayout.findViewById(R.id.tablayout);
        final ConstraintLayout container = mFrameLayout.findViewById(R.id.container);
        final Button Btnopenclose  = mFrameLayout.findViewById(R.id.btn_oc);

        svr_type = Utils.ReadEternal(getApplicationContext(),"svr");

        if(svr_type.equals("genesis")){
            section_genesis.setVisibility(View.VISIBLE);
            Btnopenclose.setTextColor(Color.parseColor("#05F60C"));
            Btnopenclose.setText("GENESIS-IP");

        } else if(svr_type.equals("memory")){
            section_mem.setVisibility(View.VISIBLE);
            Btnopenclose.setTextColor(Color.parseColor("#F44336"));
            Btnopenclose.setText("GENESIS-M");


        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();


                switch (index){
                    case 0 :  hideallTabs(); vtb1.setVisibility(View.VISIBLE); break;
                    case 1 :  hideallTabs(); vtb2.setVisibility(View.VISIBLE); break;
                    case 2 :  hideallTabs(); vtb3.setVisibility(View.VISIBLE); break;
                    case 3 :  hideallTabs(); vtb4.setVisibility(View.VISIBLE); break;
                    case 4 :  hideallTabs(); vtb5.setVisibility(View.VISIBLE); break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Btnopenclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWindowShowing){
                    container.setVisibility(View.GONE);
                    isWindowShowing = false;
                } else {
                    container.setVisibility(View.VISIBLE);
                    isWindowShowing = true;
                }
            }
        });


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Btnopenclose.performClick();
            }
        },3000);

        flyuaz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    flsct.setVisibility(View.VISIBLE);
                    GenesisTools.ActivateFomCPP("UAZFLY");

                }else {
                    flsct.setVisibility(View.GONE);
                    GenesisTools.LiveStateUAZFLY("STOP");
                }
            }
        });

        seekFly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int stateprogress = seekBar.getProgress();

                //30-65 = medium
                //65- 90 = extra height
                // 100 = sky

                if(stateprogress >30 && stateprogress <65){
                    Toast.makeText(getApplicationContext(),"MEDIUM HEIGHT",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZFLY("MEDIUM");

                }
                if(stateprogress >65 && stateprogress <90){
                    Toast.makeText(getApplicationContext(),"EXTRA HEIGHT",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZFLY("EXTRA");

                }
                if(stateprogress ==100){
                    Toast.makeText(getApplicationContext(),"SKY HIGH",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZFLY("SKYHIGH");

                }

                if(stateprogress <30 ){
                    Toast.makeText(getApplicationContext(),"NORMAL",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZFLY("NORMAL");

                }


            }
        });


        seekSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int stateprogress = seekBar.getProgress();

                //30-65 = medium
                //65- 90 = extra height
                // 100 = sky

                if(stateprogress >30 && stateprogress <65){
                    Toast.makeText(getApplicationContext(),"LIGHT SPEED",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZSPEED("LIGHT");

                }
                if(stateprogress >65 && stateprogress <90){
                    Toast.makeText(getApplicationContext(),"EXTRA SPEED",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZSPEED("EXTRA");

                }
                if(stateprogress ==100){
                    Toast.makeText(getApplicationContext(),"FERRARI SPEED",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZSPEED("FERRARI");

                }

                if(stateprogress <30 ){
                    Toast.makeText(getApplicationContext(),"NORMAL SPED",Toast.LENGTH_SHORT).show();
                    GenesisTools.LiveStateUAZSPEED("NORMAL");

                }

            }
        });

        seekRecoil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = seekBar.getProgress();

                if(progress<65){
                    GenesisTools.LiveStateRECOIL("LESSRECOIL");
                } else  if(progress>65){
                    GenesisTools.LiveStateRECOIL("NORECOIL");
                }
            }
        });


        seekHeadshot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = seekBar.getProgress();

                if(progress <35){
                    Toast.makeText(getApplicationContext(),"Low HS",Toast.LENGTH_LONG).show();
                    GenesisTools.LiveStateHeadShot("LOW");
                }

                if(progress >35 && progress<75){

                    Toast.makeText(getApplicationContext(),"Medium HS MB",Toast.LENGTH_LONG).show();
                    GenesisTools.LiveStateHeadShot("MEDIUM");

                }

                if(progress >75){
                    Toast.makeText(getApplicationContext(),"Brutal HS MB",Toast.LENGTH_LONG).show();
                    GenesisTools.LiveStateHeadShot("BRUTAL");


                }
            }
        });

        memoryBypass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("BYPASS");
                }
            }
        });

        memoryCF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(getApplicationContext(),"Game will crash if server is not connected",Toast.LENGTH_SHORT).show();
                    GenesisTools.ActivateFomCPP("VERIFY");
                }
            }
        });

        nofoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("NOFOOT");

                }
            }
        });

        server_conn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                //    GenesisTools.networkSleep();
                    GenesisTools.ActivateFomCPP("BYPASS");
                }
            }
        });

        lessrecoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("RECOIL");
                }
            }
        });

        instashot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("INSTASHOT");
                }
            }
        });

        aimbot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("AIMBOT");
                }
            }
        });

        aimhead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("AIMHEAD");
                }
            }
        });


        magic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("MAGICBULLET");
                }
            }
        });

        flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(b){

                    GenesisTools.ActivateFomCPP("SITSCOPEON");
                    //handle flash

                } else {
                    GenesisTools.ActivateFomCPP("SITSCOPEOFF");
                }
            }
        });


        wideview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("WIDEVIEW");
                }
            }
        });

        blackbody.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("BLACKBODY");
                }
            }
        });

        nofog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                 //   GenesisTools.CHEAT_DEFOG();
                }
            }
        });


        blacksky.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("NOSKY");
                }
            }
        });


        rainbowsky.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                   // GenesisTools.CHEAT_RAINSKY();
                }
            }
        });


        fastpara.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("FASTLAND");
                    Toast.makeText(getApplicationContext(),"Activating in 10secs...",Toast.LENGTH_SHORT).show();
                }
            }
        });


        wallhack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                   GenesisTools.ActivateFomCPP("WH855");
                }
            }
        });


        watercity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("WATERCITY");
                }
            }
        });

        antenna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.ActivateFomCPP("ANTENNA");
                }
            }
        });



        String salt = Utils.getSaltString();

        svrid.setText("SERVER ID : GNS-"+salt);


    }


    private void handleFlash(final Switch flash){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flsBox.isChecked()) {
                    flash.setChecked(false);
                }
            }
        },1000*15);
    }



    private void hideallTabs(){
        vtb1.setVisibility(View.GONE);
        vtb2.setVisibility(View.GONE);
        vtb3.setVisibility(View.GONE);
        vtb4.setVisibility(View.GONE);
        vtb5.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFrameLayout!=null)
            windowManager.removeView(mFrameLayout);
    }
}
