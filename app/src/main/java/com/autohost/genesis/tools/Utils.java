package com.autohost.genesis.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.autohost.genesis.MC;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Mac;

import static android.content.ContentValues.TAG;

public class Utils {


    public static String getVMDevice(Context context){


        // return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


        //add or retrieve custom device identifier

        String rd = readExternal("/storage/emulated/0/vxposed/",".d");
        if(rd.isEmpty() || !rd.contains("DD")){
            int md = new Random().nextInt(9999999);
            String nd = "DD%s".replace("%s",Integer.toString(md));
            writeToSDFile(nd,"/storage/emulated/0/vxposed/",".d");
            return nd;
        } else {
            return rd;
        }

    }


    public static String getDevice(Context context){


       /* return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String dev = "111";

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            DataInputStream  is = new DataInputStream(process.getInputStream());

            os.writeBytes("cd /data/local/genesis\n"); os.flush();
            os.writeBytes("chmod 777 genesis");
            os.writeBytes("echo 'PRINTIMEI' > script.txt\n"); os.flush();
            os.writeBytes("./genesis\n");  os.flush();


            dev = is.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return dev; */

        String imei_input = readIntegerFromFile("/storage/emulated/0/","z.ini");
        imei_input.replace(" ","").replace("\n","").trim();

        return imei_input;

    }

    public static String readExternal(String folder, String filename){
        // File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File xfile = new File(folder,filename);

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(xfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                // text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }



    public static void WriteEternal(Context context, String file, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String getQDevice(Context context){

      //  String fdir = context.getFilesDir().getAbsolutePath();
        String ext = context.getExternalFilesDir(null).getAbsolutePath();

        //"/storage/emulated/0/vxposed/"

        String rd = readFromSDCardTXT(ext,".d");
        if(rd.isEmpty() || !rd.contains("DD")){
            int md = new Random().nextInt(9999999);
            String nd = "DD%s".replace("%s",Integer.toString(md));
            writeToSDFile(nd,ext,".d");
            return nd;
        } else {
            return rd;
        }

    }




    public static void writeToSDFile(String data, String folder, String filename){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        //  File root = android.os.Environment.getExternalStorageDirectory();
        //  tv.append("\nExternal file system root: "+root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (folder);
        dir.mkdirs();
        File file = new File(dir, filename);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(data);
            //  pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // tv.append("\n\nFile written to "+file);
    }


    public static String ReadEternal(Context context,String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }

        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }



    public static String readFromSDCardTXT(String folder, String filename){
        // File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File xfile = new File(folder,filename);

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(xfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
              //  text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }


    public static String readIntegerFromFile(String folder, String filename){
        File xfile = new File(folder,filename);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(xfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
              //  text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }



    public static String decryptString(String enc){
        MC mc = new MC();

        String dec = null;

        try {
            dec = new String(mc.decrypt(enc));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dec;
    }

    public static void getRoot(){

        MC mc = new MC();

        File vxposed = new File("/storage/emulated/0/vxposed/");
        if(!vxposed.exists()){
            vxposed.mkdir();
        }




        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());


            outputStream.writeBytes("mkdir -p /storage/emulated/0/vxposed/downloads/\n");
            outputStream.flush();
            outputStream.writeBytes("echo 0 > /storage/emulated/0/vxposed/receiver.ini\n");
            outputStream.flush();
            outputStream.writeBytes("su -c iptables -F\n");
            outputStream.flush();



            outputStream.writeBytes("exit\n");
            outputStream.flush();

            GenesisTools.DeleteBP();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void cpallfiles(String source,String dest){
        String[] commands = new String[]{

                "cp -R %s* %d ".replace("%s",source).replace("%d",dest),
                "sleep 1"

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

    public static void chmod(String file,String perm){
        String[] commands = new String[]{

                "chmod %c %f ".replace("%c",perm).replace("%f",file),
                "sleep 1"

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

    public static void removeDirectory(String path){
        String[] commands = new String[]{

                "rm -rf "+path,
                "sleep 1"

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


    public static void Anti10m(Context context, boolean state){

        String pkg = ReadEternal(context,"game.ini");
        int uid = 0;
        try {
            uid = context.getPackageManager().getApplicationInfo(pkg, 0).uid;
            Toast.makeText(context,"UID : "+uid,Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String[] commands_on = new String[]{

                "su -c iptables -I OUTPUT -m owner --uid-owner %s -p tcp -j DROP".replace("%s",""+uid),

        };

        String[] commands_off = new String[]{

                "su -c iptables -D OUTPUT -m owner --uid-owner %s -p tcp -j DROP".replace("%s",""+uid),


        };

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());



            if(state){
                for(String cmd : commands_on){
                    os.writeBytes(cmd+"\n"); os.flush();

                }
            } else {
                for(String cmd : commands_off){
                    os.writeBytes(cmd+"\n"); os.flush();

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
