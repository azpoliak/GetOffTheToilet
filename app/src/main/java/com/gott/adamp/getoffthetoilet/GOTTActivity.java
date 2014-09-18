package com.gott.adamp.getoffthetoilet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.widget.TextView;
import java.util.Timer;


import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class GOTTActivity extends Activity {


    private long timeElapsed;
    public static long countDownInMinutes;
    private boolean timerHasStarted = false;
    private TextView text;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gott);

        text = (TextView) this.findViewById(R.id.timeLeft);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_minutes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                countDownInMinutes = position + 1;
                text.setText(String.valueOf(countDownInMinutes) + " Minutes till Shutdown");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        timer = new CountDownTimer(countDownInMinutes * 60000 , 1000) {
            public void onTick(long millisUntilFinished) {
                text.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                text.setText("Finished");
                //TODO - set up turning off the phone
                turnOffPhone();
            }
        };
        timer.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gott, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startClock(View view) {
        if (!timerHasStarted) {
            timer.start();
            timerHasStarted = true;
            text.setText("Time Left: " + String.valueOf(countDownInMinutes));
            timer.onTick(countDownInMinutes * 60000);


        } else {
            timer.cancel();
            timerHasStarted = false;
            text.setText("Time Left: " + String.valueOf(countDownInMinutes));
            timer.onTick(countDownInMinutes * 60000);
        }
    }

    private void turnOffPhone() {

    }




}
