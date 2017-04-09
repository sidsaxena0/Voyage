package com.sid.voyage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by sidsa_000 on 7/16/2016.
 */
public class MyUtil {

    static SharedPreferences prefs;
    static ArrayList<String> fav= new ArrayList<>();

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


    public static void saveTempPath(Context context,String path)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("APP", context.MODE_PRIVATE).edit();
        editor.putString("path", path);
        editor.apply();
    }


    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }



    public static String getPath(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("APP", context.MODE_PRIVATE);
        return  prefs.getString("path", null);
    }


    public static String[] getReports(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("APP", context.MODE_PRIVATE);
        String s =   prefs.getString("reports", "na");

        if (s.equalsIgnoreCase("na"))
            return null;
        else
        {
            if (!s.contains(","))
                return  new String[]{s};
            else
                s.split(",");
        }

        return null;
    }


    public static void saveReports(Context context, String id)
    {
        SharedPreferences prefs = context.getSharedPreferences("APP", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        String s =   prefs.getString("reports", "na");

        if (s.equalsIgnoreCase("na"))
        {
            editor.putString("reports",id);
        }
        else
        {
            s = s+","+id;
            editor.putString("reports",s);

        }


        editor.apply();

    }


    public static void saveUser(Context context, String user)
    {

            SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
            editor.putString("user", user);
            editor.apply();

    }






    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }




    static void refreshUserInfo(final Context context, String id)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(id).keepSynced(true);
            mDatabase.addListenerForSingleValueEvent(

                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            try {
                                JSONObject object = new JSONObject();

                                object.put("name",dataSnapshot.child("name").getValue());
                                object.put("email",dataSnapshot.child("email").getValue());
                                object.put("phone",dataSnapshot.child("phone").getValue());
                                object.put("country",dataSnapshot.child("country").getValue());
                                object.put("image",dataSnapshot.child("image").getValue());


                                if (dataSnapshot.hasChild("fav_breeds"))
                                {
                                    SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
                                    editor.putString("fav", String.valueOf(dataSnapshot.child("fav_breeds").getValue()));
                                    editor.apply();


                                    String[] favs = new String[0];

                                    String f = String.valueOf(dataSnapshot.child("fav_breeds").getValue()).replaceAll(" ","");
                                    if (f.contains(","))
                                        favs = f.split(",");
                                    else
                                        favs[0]= f;

                                    Collections.addAll(fav, favs);



                                }

                                MyUtil.saveUser(context,object.toString());



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Tag", "getUser:onCancelled", databaseError.toException());
                        }
                    });

        }


//        user.getReference("")
    }



    public static void savePref(Context context, String key, String value)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }


    public static void savePref(Context context, String key, int value)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();

    }




    static void savePref(Context context, String key, boolean value)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("APP", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();

    }



    public static boolean getPrefBool(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        return  prefs.getBoolean(key, false);


    }



    public static int getPrefInt(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        return  prefs.getInt(key, 0);


    }


    public static String getPref(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        return  prefs.getString(key, "na");


    }


    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
