package jullendgatc.punyatemenv4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplasScreen extends Activity {

    private static int SPLASH_TIME_OUT = 5000;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas_screen);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplasScreen.this, HomeStart.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
