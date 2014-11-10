package com.gott.adamp.getoffthetoilet;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {


    public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private long timeElapsed;
    public static long countDownInMinutes;
    private boolean timerHasStarted = false;
    private TextView text;
    private CountDownTimer timer;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            public void onItemSelected(AdapterView<?> arg0, View arg1, int inputPosition, long arg3) {
                position = inputPosition;
                countDownInMinutes = position + 1;
                text.setText(String.valueOf(countDownInMinutes) + " Minute(s) till Shutdown");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
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
        //toggle button
        if (timerHasStarted) {
            //stop the countdown
            timer.cancel();
            countDownInMinutes = position + 1;
            text.setText(String.valueOf(countDownInMinutes) + " Minute(s) till Shutdown");
            Button b1 = (Button)this.findViewById(R.id.button);
            b1.setText("START COUNTDOWN");
            timerHasStarted = false;
        } else {
            startCountDownNotification(getApplicationContext());
            //changed from 60000 to 60 just for testing purposes
            timer = new CountDownTimer(countDownInMinutes * 60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    text.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    if ((millisUntilFinished / 1000) == 60) {
                        sendOneMinWarning(getApplication());
                    }
                }

                public void onFinish() {
                    turnOffPhone();

                    text.setText("Finished");
                    //TODO - set up turning off the phone
                }
            };
            timer.start();
            /*TO-DO
                Disable starting another countdown
                change to pause countdown
           */
            timerHasStarted = true;
            Button b1 = (Button)this.findViewById(R.id.button);
            b1.setText("CANCEL COUNTDOWN");
        }
    }

    private void turnOffPhone() {

        // Intent myIntent = new Intent(MainActivity.this, ACTION_SHUTDOWN);
        // startActivity(myIntent);
        Intent myIntent = new Intent(MainActivity.this, CountDownActivity.class);
        for (int i = 0; i < 10000; i++) {
         //   startActivity(myIntent);
        }

    }

    public void startCountDownNotification(Context c) {

    }

    /**
     * Method to make the notification that they're running low
     *
     * @param c the context passed by the doNotificationLogic method
     */
    public void sendOneMinWarning(Context c) {
        String title = "GetOffTheToilet";
        String subject = "Warning";
        String body = "Restarting in a minute";
        NotificationManager NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification(R.drawable.ic_launcher
                ,title,System.currentTimeMillis());
        PendingIntent pending=PendingIntent.getActivity(
                getApplicationContext(),0, new Intent(),0);
        notify.setLatestEventInfo(getApplicationContext(),subject,body,pending);
        NM.notify(0, notify);

    }
}
