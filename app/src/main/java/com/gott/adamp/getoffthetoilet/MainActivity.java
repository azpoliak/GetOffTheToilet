package com.gott.adamp.getoffthetoilet;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Button b1;
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotifyBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) this.findViewById(R.id.timeLeft);

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }

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
                String msg = countDownInMinutes > 1 ? "Minutes" : "Minute";
                text.setText(String.valueOf(countDownInMinutes) + " " + msg + " till Shutdown");
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

    public Button getButton() {
        if (b1 == null) {
            b1 = (Button)this.findViewById(R.id.button);
        }
        return b1;
    }

    public void startClock(View view) {
        //toggle button
        if (timerHasStarted) {
            //stop the countdown
            stopService(new Intent(getBaseContext(), RestartService.class));

            countDownInMinutes = position + 1;
            text.setText(String.valueOf(countDownInMinutes) + " Minute(s) till Shutdown");
            Button b1 = (Button)this.findViewById(R.id.button);
            b1.setText("START COUNTDOWN");
            timerHasStarted = false;
        } else {
            Intent myIntent = new Intent(getBaseContext(), RestartService.class);
            myIntent.putExtra("countDown", countDownInMinutes);
            startService(myIntent);

            timerHasStarted = true;
            Button b1 = (Button)this.findViewById(R.id.button);
            b1.setText("CANCEL COUNTDOWN");
        }
    }
}
