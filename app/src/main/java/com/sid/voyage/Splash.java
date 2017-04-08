package com.sid.voyage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView textView = (TextView)findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"code.otf");
        textView.setTypeface(typeface);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                startActivity(new Intent(Splash.this,MainActivity.class));

                finish();

            }
        }, 9000);
    }
}
