package com.sid.voyage.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sidsa on 11/19/2016.
 */

public class MyUtil {

        public static Bitmap bitmap;

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }




    public static Uri saveFile(Activity context, Bitmap bitmap)
    {
        OutputStream fOut = null;
        Uri outputFileUri=null;
        try {
            File root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temp"+File.separator + "pubapp" + File.separator);
            boolean isDirectoryCreated  = root.mkdirs();
            Log.d("Directory","Directory "+root.getPath()+" "+isDirectoryCreated);

            saveTempPath(context,root.getPath());


            File sdImageMainDirectory = new File(root, System.currentTimeMillis()+".jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);

        } catch (Exception e) {
            //Toast.makeText(activity, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }




        return outputFileUri;
    }


    public static void clearPrefrences(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }


    public static void saveTempPath(Context context,String path)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.putString("path", path);
        editor.apply();
    }


    @SuppressLint("NewApi")
    public static int getDeviceWidth(Activity activity) {
        int deviceWidth = 0;

        Point size = new Point();
        WindowManager windowManager = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceWidth = size.x;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceWidth = display.getWidth();
        }
        return deviceWidth;
    }

    @SuppressLint("NewApi")
    public static int getDeviceHeight(Activity activity) {
        int deviceHeight = 0;

        Point size = new Point();
        WindowManager windowManager = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceHeight = size.y;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceHeight = display.getHeight();
        }
        return deviceHeight;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }



    public   static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st" };





    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void getAddressFromLocation(final double latitude, final double longitude, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    Log.d("Receved","Just got lat long trying to find address Lat : "+latitude+ " Long: "+longitude);

                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        address.getSubLocality();
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = address.getSubLocality()+", "+address.getLocality();
                    }
                } catch (IOException e) {
                    Log.e("GEOCODER", "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", "Unable to get location");
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }



    public static void saveUser(Context context, String user)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.putString("user", user);
        editor.apply();

    }



    public static String getUser(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE);

        return prefs.getString("user","na");

    }


    public static void saveString(Context context,String key, String value)
    {
        if(context!=null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.apply();
        }


    }


    public static void saveBoolean(Context context,String key, boolean value)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("APP_BOOLS", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public static boolean getBoolean(Context context,String key)
    {

        SharedPreferences shared = context.getSharedPreferences("APP_BOOLS", Context.MODE_PRIVATE);
        return shared.getBoolean(key,false);

    }



    public  static String getString(Context context, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE);

        return prefs.getString(key,"na");

    }


}
