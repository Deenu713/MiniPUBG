package com.autohost.genesis.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.autohost.genesis.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class GenesisTools {

    static String ZDTENC = "5bb7837d2aca0ab9309296058ceeaa9ac190cb731d5da42f7b19e92e5b5bc7c0"; //   /d/l/g/


    public static void ActivateFomCPP(String function){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f",function).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void classicNDK(String function){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f",function).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();

         //   process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void runSingleCommand(String command){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("%s\n".replace("%s",command)); os.flush();
            os.writeBytes("exit\n"); os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }


    public static void EXECPP(String cpp){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("chmod 777 %d%f\n".replace("%d",DATA_DIRECTORY).replace("%f",cpp)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./%f &>/dev/null\n".replace("%d",DATA_DIRECTORY).replace("%f",cpp)); os.flush();

            os.writeBytes("exit\n"); os.flush();

            //   process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void opcheat(String function){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f",function).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void sopcheat(String f, String file, Context context, int audio){

        Utils.WriteEternal(context,file,f);
        final MediaPlayer player = MediaPlayer.create(context, audio);
        player.start();
    }

    public static void opmod(String f,String file){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("echo '%f' > %d%g\n".replace("%f",f).replace("%g",file).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("exit\n"); os.flush();

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void DeleteBP(){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes( "rm -rf %dbp.ini\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void VIPDRM(){

        String ENC_CMD0 = "1895a54b33b9c814f09e431140e462178b699ee36909554dc6492cbb17a96ecf";
        String ENC_CMD1 = "e30e47f9966b2dcbc56d3f2cde68a404dcf643eff92670b048a9051f109bf97b22a719d3ff1e1386ffb0f4fa7050bd5d";


        String DEC_CMD0 = Utils.decryptString(ENC_CMD0);
        String DEC_CMD1 = Utils.decryptString(ENC_CMD1);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes(DEC_CMD0+"\n"); os.flush();
            os.writeBytes(DEC_CMD1+"\n"); os.flush();

            os.writeBytes("exit\n"); os.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void SetupNewFiles(){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("rm -rf %d*\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("sleep 1\n"); os.flush();

            os.writeBytes( "mv /sdcard/vxposed/cdata/* %d\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("rm -rf /sdcard/vxposed/cdata.zip\n"); os.flush();

            os.writeBytes("exit\n"); os.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SetupNewFilesA11(Context context){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);

        String ext = context.getExternalFilesDir(null).getAbsolutePath();
      //  Toast.makeText(context,ext,Toast.LENGTH_SHORT).show();

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("rm -rf %d*\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("sleep 1\n"); os.flush();

            os.writeBytes( "mv %e/cdata/* %d\n".replace("%d",DATA_DIRECTORY).replace("%e",ext)); os.flush();
            os.writeBytes("rm -rf %e/cdata.zip\n".replace("%e",ext)); os.flush();
            os.writeBytes("rm -rf %e/*MAC*\n".replace("%e",ext)); os.flush();


            os.writeBytes("exit\n"); os.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void LiveStateUAZFLY(String state){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("echo '%s'>%dfly.ini\n".replace("%s",state).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("exit\n"); os.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void LiveStateUAZSPEED(String state){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);



        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("echo '%f' > %dspeed.ini\n".replace("%f",state).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f","UAZSPEED").replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LiveStateRECOIL(String state){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("echo '%f' > %drecoil.ini\n".replace("%f",state).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f","RECOIL").replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void LiveStateHeadShot(String state){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("echo '%f' > %dheadshot.ini\n".replace("%f",state).replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("echo '%f' > %dscript.txt\n".replace("%f","MAGICBULLET").replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("chmod 777 %dgenesis\n".replace("%d",DATA_DIRECTORY)); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./genesis &>/dev/null\n".replace("%d",DATA_DIRECTORY)); os.flush();

            os.writeBytes("exit\n"); os.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void StartGameWithIP(Context context , String pkg){

        String packages = Utils.ReadEternal(context,"game.ini");
        int uid = 0;
        try {
            uid = context.getPackageManager().getApplicationInfo(packages, 0).uid;
            Toast.makeText(context,"UID : "+uid,Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void APIBP(Context context, String key){

        String DATA_DIRECTORY = Utils.decryptString(ZDTENC);


        File x = new File(context.getFilesDir(),"stream.ini");
        String streamlink = x.getAbsolutePath();

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());


            os.writeBytes("chmod 777 %d%f\n".replace("%d",DATA_DIRECTORY).replace("%f","api")); os.flush();
            os.writeBytes("cd %d\n".replace("%d",DATA_DIRECTORY));  os.flush();
            os.writeBytes("./api %s 1 %f\n".replace("%s",key).replace("%f",streamlink)); os.flush();



            //   process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
