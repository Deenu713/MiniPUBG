package com.autohost.genesis;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.autohost.genesis.tools.GenesisTools;
import com.autohost.genesis.tools.Utils;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.animation.Animation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;


public class ClassicService extends Service  {


    private WindowManager windowManager;
    private WindowManager.LayoutParams params,fparams,sparams;
    private boolean isWindowShowing,isRoot = true;
    private Handler mHandler = new Handler();
    private CheckBox flsBox;
    private FrameLayout mFrameLayout;
    private String svr_type;
    private View mainview,fullview,streamview;
    private LinearLayout streambar;

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private boolean isBMenuShowing = false;
    private boolean isInGame,isBypassed = false;

    private BroadcastReceiver receiver;
    Timer timer = new Timer();
    boolean isAimOn = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable streamsegment = new Runnable() {
        @Override
        public void run() {
            getStreamInput();
            mHandler.postDelayed(streamsegment,100);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mainview = inflater.inflate(R.layout.injector_classic,null);
        fullview  = inflater.inflate(R.layout.overlay_full,null);
        streamview = inflater.inflate(R.layout.inc_streamer,null);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity =   Gravity.TOP | Gravity.CENTER;
        params.x = -754;
        params.y = 0;

        fparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        fparams.gravity =   Gravity.CENTER;
        fparams.x = -300;
        fparams.y = 0;

        sparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        sparams.gravity =   Gravity.CENTER | Gravity.TOP;
        sparams.x = 300;
        sparams.y = 75;

        mFrameLayout = new FrameLayout(this);
        windowManager.addView(mFrameLayout, params);
        windowManager.addView(streamview, sparams);
      //  fullview.setVisibility(View.GONE);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.injector_mini, mFrameLayout);

        final LinearLayout container = mFrameLayout.findViewById(R.id.container);

        Switch vm_bypass = mFrameLayout.findViewById(R.id.switch_vmbypass);
        Switch ac_bypass = mFrameLayout.findViewById(R.id.switch_acbypass);


        String str_root = Utils.ReadEternal(getApplicationContext(),"root");
        if(str_root.equals("0")){
            isRoot = false;
            vm_bypass.setVisibility(View.VISIBLE);
            ac_bypass.setVisibility(View.VISIBLE);

        }



        final Button Btnopenclose  = mFrameLayout.findViewById(R.id.btn_oc);

        Btnopenclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWindowShowing){
                   // Animation animation5 = ViewTransitionBuilder.transit(container).scaleX(1f, 0.2f).scaleY(1f, 0f).translationX(-50f).buildAnimation();
                   // animation5.startAnimation(500);
                    container.setVisibility(View.GONE);
                    isWindowShowing = false;

                } else {
                    container.setVisibility(View.VISIBLE);
                    isWindowShowing = true;
                    Animation animation5 = ViewTransitionBuilder.transit(container).scaleX(0.2f, 1f).scaleY(0f, 1f).translationX(-50f).buildAnimation();
                    animation5.startAnimation(500);
                }
            }
        });

        String svr = Utils.ReadEternal(getApplicationContext(),"svr");




        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();





        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Btnopenclose.performClick();

            }
        },3000);

        mHandler.post(streamsegment);


        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("activating");
        progressDialog.setMessage("please wait while its done...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        progressDialog.hide();


        Button btnmv = mFrameLayout.findViewById(R.id.btnmv);

        btnmv.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mFrameLayout, params);
                        return true;

                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Toast.makeText(getApplicationContext(),"Minicheat Running...",Toast.LENGTH_SHORT).show();
                        return true;
                    //   case MotionEvent.ACTION_BUTTON_RELEASE:
                    //       Toast.makeText(ChatHeadService.this,"hello",Toast.LENGTH_SHORT).show();
                    //       return true;
                }
                return false;
            }


        });

      /*  Switch hs = mFrameLayout.findViewById(R.id.switch_hs);
        hs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(isRoot) {
                        progressDialog.show();
                        Utils.WriteEternal(getApplicationContext(), "headshot.ini", "1");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.hide();
                            }
                        }, 6000);
                    } else {
                        //non root execution
                        injetVMCheat("HEADSHOT");
                    }
                }
            }
        }); */



       /* final TextView tipsp = mFrameLayout.findViewById(R.id.sp_tip);
        Switch sp = mFrameLayout.findViewById(R.id.switch_speed);
        String ffbv = Utils.ReadEternal(getApplicationContext(),"ffb.ini");
        if(ffbv.equals("0")){

            tipsp.setVisibility(View.GONE);
            sp.setVisibility(View.GONE);
        }

        sp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(isRoot){
                    progressDialog.show();
                    Utils.WriteEternal(getApplicationContext(),"speedhack.ini","1");
                    tipsp.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    },6000);
                    } else {
                        //non root execution
                        injetVMCheat("FASTRUNON");

                    }
                } else {
                    if(isRoot){
                    progressDialog.show();
                    Utils.WriteEternal(getApplicationContext(),"speedhack.ini","2");
                    tipsp.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    },6000);
                    } else {
                        //non root execution
                        injetVMCheat("FASTRUNOFF");
                    }
                }
            }
        });


        Switch aim3 = mFrameLayout.findViewById(R.id.switch_aimbot);
        aim3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(isRoot){
                    progressDialog.show();
                    Utils.WriteEternal(getApplicationContext(),"aimbot.ini","1");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    },6000);
                    } else {
                        //non root execution
                    }
                }
            }
        }); */



      /*  Switch uazf = mFrameLayout.findViewById(R.id.switch_uaz);
        uazf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(isRoot){
                    progressDialog.show();
                    Utils.WriteEternal(getApplicationContext(),"uaz.ini","1");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    },6000);
                    } else {
                        //non root execution
                        injetVMCheat("UAZFLY");

                    }
                }
            }
        });

        Switch fastp = mFrameLayout.findViewById(R.id.switch_fastland);
        fastp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(isRoot){
                    progressDialog.show();
                    Utils.WriteEternal(getApplicationContext(),"fastland.ini","1");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    },6000);
                    } else {
                        //non root execution
                        injetVMCheat("FASTLAND");
                    }
                }

            }
        }); */

        final Switch bypass32 = mFrameLayout.findViewById(R.id.switch_bypass32);
        final String key = Utils.ReadEternal(getApplicationContext(),"key.txt");
        bypass32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                   // GenesisTools.EXECPP("api %k 1".replace("%k",key));
                    GenesisTools.APIBP(getApplicationContext(),key);
                    Toast.makeText(getApplicationContext(),"apply cheats only if success bypass notification comes",Toast.LENGTH_LONG).show();
                }

            }
        });

        final Switch bypass10m = mFrameLayout.findViewById(R.id.switch_bypass10m);
        bypass10m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.Anti10m(getApplicationContext(),true);
                    Toast.makeText(getApplicationContext(),"Disable in Lobby",Toast.LENGTH_LONG).show();
                } else {
                    Utils.Anti10m(getApplicationContext(),false);
                    Toast.makeText(getApplicationContext(),"Enable again at Island in next match",Toast.LENGTH_LONG).show();
                }

            }
        });

        final Switch lrall = mFrameLayout.findViewById(R.id.switch_lr);
        lrall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(isBypassed){
                    GenesisTools.EXECPP("recoil");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    lrall.setChecked(false);
                }

            }
        });


        final Switch nofoot = mFrameLayout.findViewById(R.id.switch_nofoot);
        nofoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("nofoot");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    nofoot.setChecked(false);
                }

            }
        });

        final Switch mb = mFrameLayout.findViewById(R.id.switch_mbc);
        mb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("magicbullet");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    mb.setChecked(false);
                }
            }
        });

        final Switch mb2 = mFrameLayout.findViewById(R.id.switch_mbc2);
        mb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("magicbullet2");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    mb2.setChecked(false);
                }
            }
        });


        final Switch instashot = mFrameLayout.findViewById(R.id.switch_instanthit);
        instashot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("instashot");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    instashot.setChecked(false);
                }
            }
        });

        final Switch fastdata = mFrameLayout.findViewById(R.id.fast_data);
        fastdata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("fastdata");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    fastdata.setChecked(false);
                }
            }
        });

        final ImageView fovview = new ImageView(getApplicationContext());
        fovview.setBackgroundResource(R.drawable.aim_fov);
        final Switch aim = mFrameLayout.findViewById(R.id.switch_aimbot);
        final ImageButton aim_switch = new ImageButton(getApplicationContext());
        aim_switch.setBackgroundResource(R.drawable.aim_on);
        aim_switch.setAlpha(0.7f);


        aim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    if(b){
                        GenesisTools.EXECPP("aimbot_on");

                     }  else {
                        GenesisTools.EXECPP("aimbot_off");

                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    aim.setChecked(false);
                }
            }
        });



        final Switch flashplayer = mFrameLayout.findViewById(R.id.switch_flash);
        flashplayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    if(b){
                        GenesisTools.EXECPP("flash_on");
                        Toast.makeText(getApplicationContext(),"Contact Admins for full flash access @mohatirxxx",Toast.LENGTH_LONG).show();
                    } else {
                        GenesisTools.EXECPP("flash_off");
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    flashplayer.setChecked(false);
                }
            }
        });

        final Switch antishake = mFrameLayout.findViewById(R.id.switch_antishakegun);
        antishake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isBypassed){
                    GenesisTools.EXECPP("antishake");
                } else {
                    Toast.makeText(getApplicationContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    antishake.setChecked(false);
                }
            }
        });





    }


    void shouldDisableLater(final Switch s){
      /*  mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                s.setChecked(false);
            }
        },1000*60); */
    }

    void resetSwitches(Switch s1, Switch s2, Switch s3, Switch s4, Switch s5){
        if(s1.isChecked()){
            s1.setChecked(false);
        }
        if(s2.isChecked()){
            s2.setChecked(false);
        }
        if(s3.isChecked()){
            s3.setChecked(false);
        }
        if(s4.isChecked()){
            s4.setChecked(false);
        }
        if(s5.isChecked()){
            s5.setChecked(false);
        }

    }

    private String st_lastmsg;
    private void getStreamInput(){

       // File f = new File("/data/data/com.tencent.iggenesis/stream.ini");
        String lastline = null;
        String sCurrentLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader("/data/data/com.minipubg/files/stream.ini"));
            while ((sCurrentLine = br.readLine()) != null) {
                lastline = sCurrentLine;
            }

            String s = lastline;

            if(!s.equals(st_lastmsg)){
                st_lastmsg = s;
                if(s.contains("Bypassed")){
                    isBypassed = true;
                }
                AddFadingText(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void AddFadingText(String text){
        final TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(11f);
        textView.setPadding(160,10,10,10);
        textView.setGravity(Gravity.LEFT|Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.infobg);
        final LinearLayout xv = streamview.findViewById(R.id.streambar);
        xv.addView(textView);
        textView.getLayoutParams().height = 105;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                xv.removeView(textView);
            }
        },1000*15);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mShakeDetector);
//        unregisterReceiver(receiver);
        if(mFrameLayout!=null)
            windowManager.removeView(mFrameLayout);
        if(fullview!=null)
            windowManager.removeView(fullview);
        if(streamview!=null)
            windowManager.removeView(streamview);

        timer.cancel();;
    }

    private void injetVMCheat(String func){

        String cheatDir = getFilesDir().getAbsolutePath()+"/cpp";

        String[] commands = new String[]{

                "echo '%s' > /storage/emulated/0/vxposed/script.txt".replace("%s",func),
                "cd %d".replace("%d",cheatDir),
                "./medusa"

        };
        try {
            Process process = Runtime.getRuntime().exec("sh");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            for(String cmd : commands){
                os.writeBytes(cmd+"\n"); os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loginbypass(){
        String data = Utils.ReadEternal(getApplicationContext(),"libs.ini");
        String[] fUri = data.split(",");

        //  Toast.makeText(getApplicationContext(),fUri.length+"",Toast.LENGTH_LONG).show();
        for(String uri : fUri){
            Utils.removeDirectory(uri);
        }
    }


}
