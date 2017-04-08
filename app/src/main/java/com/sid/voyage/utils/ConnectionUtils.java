package com.sid.voyage.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sidsa on 11/21/2016.
 */

public class ConnectionUtils {

    Context context;
    private OkHttpClient client = new OkHttpClient();
    private boolean PRODUCTION_ENABLED;

    public ConnectionUtils(Context context)
    {
        this.context = context;
    }

    public void makePostRequest(final ResponseCallback back, String url, RequestBody formBody)
    {
        if (MyUtil.isNetworkAvailable(context))
        {
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {


                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            back.onFailed(e);


                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String res = response.body().string();
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (!PRODUCTION_ENABLED)
                                Log.d("Response","Server Response is "+res);
                            back.onResponse(res);


                        }
                    });
                }
            });
        }
        else
            showDialogue();

    }


    public void makeGetRequest(final ResponseCallback back, String url)
    {
        if (MyUtil.isNetworkAvailable(context))
        {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            back.onFailed(e);


                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String res = response.body().string();

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (!PRODUCTION_ENABLED)
                                Log.d("Response","Server Response is "+res);
                            back.onResponse(res);

                        }
                    });


                }
            });
        }
        else
            showDialogue();
    }


    private void showDialogue()
    {
        if (context!=null)
        {
            //show dialogue

            new MaterialDialog.Builder(context)
                    .title("No Connection")
                    .content("Hi, Looks like you are not connected to a network, please make sure you have a working internet connection.")
                    .positiveText("Retry")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            if (MyUtil.isNetworkAvailable(context))
                                Toast.makeText(context, "Connection established!", Toast.LENGTH_SHORT).show();
                            else
                                showDialogue();
                        }
                    })
                    .show();

        }
    }



}
