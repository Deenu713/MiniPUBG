package com.autohost.genesis.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.autohost.genesis.ClassicService;
import com.autohost.genesis.GenesisService;
import com.autohost.genesis.R;
import com.autohost.genesis.Server;
import com.autohost.genesis.tools.GenesisTools;
import com.autohost.genesis.tools.Utils;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stream.customalert.CustomAlertDialogue;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoreFragment extends Fragment {

    private String DATA_DIRECTORY = null;
    private String LOCALVERSION = null;
    private Handler mHandler = new Handler();
    private boolean isDownloaded,isVMloaded,isVMOS = false;
    private TextView status;
    private RadioGroup groupGameVersion,groupBypassServer;
    private boolean isRoot = true;
    private View mainview;
    private String desc_gametype,desc_ostype,shellName;
    private String IMEI;
    private String VM_ROOTDIR,VM_GAMELIBDIR,VM_CHEATDIR;
    private List<String> vmlibs = new ArrayList<>();
    private ProgressDialog progressDialog;


    public CoreFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mainview = inflater.inflate(R.layout.fragment_core, container, false);

        status = mainview.findViewById(R.id.textView_status);
        TextView txtmode = mainview.findViewById(R.id.txt_mode);
        Button stop = mainview.findViewById(R.id.button_stop);
        Button btnloadvritual = mainview.findViewById(R.id.button_loadvm);
        LinearLayout a7menu = mainview.findViewById(R.id.a7menu);
        TextView txtgamever = mainview.findViewById(R.id.txt_gametype);



        String ZDTENC = "5bb7837d2aca0ab9309296058ceeaa9ac190cb731d5da42f7b19e92e5b5bc7c0"; //   /d/l/g/

        DATA_DIRECTORY = Utils.decryptString(ZDTENC);

        new wgetnewFile().execute();

        String str_root = Utils.ReadEternal(getContext(),"root");
        String str_vmos = Utils.ReadEternal(getContext(),"vmos");
        String str_game = Utils.ReadEternal(getContext(),"game.ini");
        if(str_root.equals("0")){
            isRoot = false;
            txtmode.setText("☐ Virtual Mode");
            btnloadvritual.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
        }

        if(str_vmos.equals("1")){
            a7menu.setVisibility(View.VISIBLE);
            isVMOS = true;
        }

        if(str_game.equals("com.tencent.ig")){
            txtgamever.setText("GLOBAL PUBG");
        } else if(str_game.equals("com.pubg.krmobile")){
            txtgamever.setText("KOREA PUBG");
        }

        Button play = mainview.findViewById(R.id.button_play);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRoot){
                    if(isDownloaded){
                        play32();
                    }
                } else {
                    if(isDownloaded && isVMloaded && isPUBGInstalled()){
                        play32();
                    } else {
                        MessageBoard("Error!","Virtual Not Loaded or Data not downloaded or PUBG not cloned.\nContact Customer support for help");
                    }
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getContext(),ClassicService.class));
                getActivity().deleteFile("stream.ini");
                MessageBoard("MiniCheat","Cheats Stopped");

            }
        });

        btnloadvritual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputDlVmPkg();
            }
        });



        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("activating");
        progressDialog.setMessage("please wait while its done...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.hide();
        getActivity().deleteFile("stream.ini");


        init_a7menu(mainview);

        return mainview;
    }




    void play32(){

        String xms = "";
        if(isRoot){
            desc_gametype = "#ROOT MODE";
            xms = "Playing in Root Mode. Change device ID after playing 20 games";
        } else {
            desc_gametype = "☐ Virtual Mode";
            xms = "Playing in Virtual Mode. Reclone PUBG after stop playing. Clear original game data after 20 games";
        }

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getContext())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(desc_gametype)
                .setMessage(xms)
                .setNegativeText("Cancel")
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Play")
                .setPositiveColor(R.color.colorGreen)
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();


                        if(!isVMOS){
                            getActivity().startService(new Intent(getContext(),ClassicService.class));

                        }

                        Toast.makeText(getContext(),"Starting Game....",Toast.LENGTH_LONG).show();
                        if(isRoot){
                          //  GenesisTools.EXECPP("minigl");
                            String gameversion = Utils.ReadEternal(getContext(),"game.ini");
                            String shname  = "";
                            if(gameversion.equals("com.tencent.ig")){
                                shname = "on.sh";
                            } else if(gameversion.equals("com.pubg.krmobile")){
                                shname = "onkr.sh";
                            }
                            Utils.WriteEternal(getContext(),"stream.ini","Welcome to Mini PUBG");
                            Toast.makeText(getContext(),"Please wait 5sec game starting....",Toast.LENGTH_LONG).show();
                            GenesisTools.runSingleCommand("sh /data/local/genesis/%f".replace("%f",shname));
                        } else {
                            playVM();
                        }
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();

    }

    void playVM(){

        //start game + much more

        final Intent game = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.ig");
        Toast.makeText(getContext(),"Bypass Virtual immediatly at PUBG logo then bypass anticheat at lobby",Toast.LENGTH_LONG).show();
        startActivity(game);

    }






    private class wgetnewFile extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {

            String url = "http://autohost.tech/minipubg/07/getlatestfileinfo.php";
            HashMap<String,String> hashMap = new HashMap<>();

            return Server.performPostCall(url,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            LOCALVERSION = Utils.ReadEternal(getContext(),"dl");

            if(!s.isEmpty()){
                if(!LOCALVERSION.equals(s)){

                    ShowDownloadAlert();
                    LOCALVERSION = s;
                } else {
                    isDownloaded  = true;
                  //  MessageBoard("Data on off Required once","Do data on off once (once only) after gun pick and before killing any player, to avoid reports and 10m ban\nYou can use all brutal hacks");
                }
            }
        }
    }


    private void ShowDownloadAlert(){


        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getContext())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle("Server update")
                .setMessage("New Update is Available\nSince this is new update check safety using fake account first")
                .setCancelable(false)
                .setPositiveText("Install Update")
                .setPositiveColor(R.color.colorBlue)
                .setTitleColor(R.color.colorGreen)
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        dloadSD();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();

    }

    private String tdir = null;
    private void dloadSD(){

        status.setVisibility(View.VISIBLE);
        String device;

        if (Build.VERSION.SDK_INT ==30){
            tdir = getContext().getExternalFilesDir(null).getAbsolutePath();
            device = Utils.getQDevice(getContext());
        } else {
            tdir = "/storage/emulated/0/vxposed/";
            device = Utils.getDevice(getContext());
        }

        String key = Utils.ReadEternal(getContext(),"key");


        String url;

        if(isRoot){
            url = "http://autohost.tech/minipubg/07/getnewfile.php?key="+key+"&device="+device;
        } else {
            url = "http://autohost.tech/minipubg/07/getnewfile2.php?key="+key;
        }


        PRDownloader.download(url,tdir,"cdata.zip")
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long bits = progress.currentBytes;
                        long total = progress.totalBytes;

                        status.setText("Downloading files...."+bits);
                        // Toast.makeText(getContext(),"Downloading",Toast.LENGTH_SHORT).show();
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String md = null;
                        if (Build.VERSION.SDK_INT ==30){
                             md = "/sdcard/Android/data/com.tencent.iggenesis/files/";
                        } else {
                             md = "/storage/emulated/0/vxposed/";
                        }



                        Utils.unpackZip(md,"cdata.zip");
                        status.setVisibility(View.GONE); status.setText("connecting server...");

                       mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.WriteEternal(getContext(),"dl",LOCALVERSION);

                                if(isRoot){
                                    GenesisTools.SetupNewFiles();
                                } else {
                                    loadVMCPP();
                                }


                                MessageBoard("Download Status","Download finished. Restart App");
                            }
                        },1000*2);



                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getContext(),"Download failed Restart app",Toast.LENGTH_LONG).show();
                        // DownloadNotice();
                        getActivity().finish();
                    }



                });

    }


    private void MessageBoard(String title,String msg){



        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getContext())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveText("OK")
                .setPositiveColor(R.color.colorBlack)
                .setTitleColor(R.color.colorBlue)
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();

                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }


    private void inputDlVmPkg(){

        final EditText editText = new EditText(getContext());
        editText.setText("com.tencent.virtual");

        new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle("Virtual Package Name")
                .setMessage("Please paste your virtual package name to continue")
                .setView(editText)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cpkg = editText.getText().toString();
                        if(isDownloaded) {
                            loadVM(cpkg);
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .create().show();

    }

    private void loadVM(String pkg){

        File file = new File("/data/user/0/%p".replace("%p",pkg));
        if(file.exists()){
            VM_ROOTDIR = file.getAbsolutePath();
            //   Toast.makeText(getApplicationContext(),getFilesDir().getAbsolutePath(),Toast.LENGTH_LONG).show();
            scanlibs(VM_ROOTDIR,".so");
            if(vmlibs.size() <9){
                MessageBoard("Anti Virtual Fail!","Failed to Load Virtual Detection files\nEither game not cloned or virtual package incorrect\nTry Recloning the game");
            } else {
                isVMloaded = true;
                MessageBoard(pkg," virtual data load success!");
                Utils.WriteEternal(getContext(),"libs.ini",vmlibs.toString().replace("[","").replace("]",""));
            }
        } else {
            MessageBoard("Incorrect Package name","Failed to detect virtual, check virtual package entered and try again with correct virtual package name\nUse DevTools app to find package name\ndefault package name is set for Sameer MultiSpace Virtual 9.0");
        }
    }


    private String scanlibs(String str_targetdir, final String str_search){

        File targetdir = new File(str_targetdir);
        String searched  = null;

        File[] children = targetdir.listFiles(new FileFilter() {  //lists the folders in a directory or the file
            public boolean accept(File file) {

                return file.isDirectory() || file.getName().contains(str_search);

            }
        });

        if(children!=null)
            for (File f : children) {

                if (f.getName().contains(str_search)) {
                    vmlibs.add(f.getAbsolutePath());
                    searched = f.getAbsolutePath();
                    scanlibs(f.getAbsolutePath(), str_search);
                } else {
                    scanlibs(f.getAbsolutePath(), str_search);
                }

            }

        return searched;
    }

    private void loadVMCPP(){
        //  Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
        File cfd = new File(getActivity().getFilesDir().getAbsolutePath()+"/cpp");
        if(!cfd.exists()){
            cfd.mkdir();
        }

        VM_CHEATDIR = cfd.getAbsolutePath();
        Utils.cpallfiles("/storage/emulated/0/vxposed/cdata/",VM_CHEATDIR);

        try {
            Thread.sleep(1000*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utils.chmod(VM_CHEATDIR+"/*","777");


        File cppf = new File(VM_CHEATDIR,"medusa");

        if(!cppf.exists()){
            MessageBoard("Failed to Load Cheats","Failed to load cheat files\nEither files are not downloaded or problem with the virtual\n"+cppf.getAbsolutePath());
            isDownloaded = false;
        }

    }

    private boolean isPUBGInstalled(){
        final Intent game = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.ig");

        return game != null;
    }


    void init_a7menu(View mFrameLayout){

        final Switch bypass32 = mFrameLayout.findViewById(R.id.switch_bypass32);
        bypass32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    GenesisTools.EXECPP("bypass32");
                    Toast.makeText(getContext(),"apply cheats only if success bypass notification comes",Toast.LENGTH_LONG).show();
                }

            }
        });

        final Switch lrall = mFrameLayout.findViewById(R.id.switch_lr);
        lrall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("recoil");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    lrall.setChecked(false);
                }

            }
        });


        final Switch nofoot = mFrameLayout.findViewById(R.id.switch_nofoot);
        nofoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("nofoot");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    nofoot.setChecked(false);
                }

            }
        });

        final Switch mb = mFrameLayout.findViewById(R.id.switch_mbc);
        mb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("magicbullet");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    mb.setChecked(false);
                }
            }
        });

        final Switch flashplayer = mFrameLayout.findViewById(R.id.switch_flash);
        flashplayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    if(b)
                        GenesisTools.EXECPP("flash_on");
                    else
                        GenesisTools.EXECPP("flash_off");

                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    flashplayer.setChecked(false);
                }
            }
        });

        final Switch aim = mFrameLayout.findViewById(R.id.switch_aimbot);
        aim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){

                    if(b){
                        GenesisTools.EXECPP("aimbot_on");
                    }

                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    mb.setChecked(false);
                }
            }
        });

        final Switch fastdata = mFrameLayout.findViewById(R.id.fast_data);
        fastdata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("fastdata");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    fastdata.setChecked(false);
                }
            }
        });

        final Switch bypass10m = mFrameLayout.findViewById(R.id.switch_bypass10m);
        bypass10m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.Anti10m(getContext(),true);
                    Toast.makeText(getContext(),"Disable in Lobby",Toast.LENGTH_LONG).show();
                } else {
                    Utils.Anti10m(getContext(),false);
                    Toast.makeText(getContext(),"Enable again at Island in next match",Toast.LENGTH_LONG).show();
                }

            }
        });

        final Switch mb2 = mFrameLayout.findViewById(R.id.switch_mbc2);
        mb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("magicbullet2");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    mb2.setChecked(false);
                }
            }
        });

        final Switch antishake = mFrameLayout.findViewById(R.id.switch_antishakegun);
        antishake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("antishake");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    antishake.setChecked(false);
                }
            }
        });

        final Switch antenna = mFrameLayout.findViewById(R.id.switch_antennapro);
        antenna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bypass32.isChecked()){
                    GenesisTools.EXECPP("antenna");
                } else {
                    Toast.makeText(getContext(),"Game not bypassed",Toast.LENGTH_SHORT).show();
                    antenna.setChecked(false);
                }
            }
        });


    }


}
