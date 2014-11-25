package com.gott.adamp.getoffthetoilet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RestartService extends Service {
    private long timeElapsed;
    private boolean timerHasStarted = false;
    private TextView text;
    private CountDownTimer timer;
    private int position;
    private Button b1;
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotifyBuilder;
    private long countDownInMinutes;

    public RestartService() {
    }

    /** indicates how to behave if the service is killed */
    int mStartMode;
    /** interface for clients that bind */
    IBinder mBinder;
    /** indicates whether onRebind should be used */
    boolean mAllowRebind;
    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        Log.d("RestartService : ", "OnCreate() event");

  //      turnOff();

    }
    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        countDownInMinutes = intent.getLongExtra("countDown", 1);

        turnOff();

        return START_STICKY;
    }



    private void turnOff() {
        //changed from 60000 to 60 just for testing purposes

        timer = new CountDownTimer(countDownInMinutes * 60000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("onTick : ", "" + millisUntilFinished);
                //text.setText("Seconds remaining: " + millisUntilFinished / 1000);
                if ((millisUntilFinished / 1000) == 30) {
                    Toast.makeText(getApplicationContext(), "30 seconds left", Toast.LENGTH_LONG);
                }

            }

            public void onFinish() {

                //fix this cause app crashes now when resetting
                countDownInMinutes = position + 1;
                // text.setText(String.valueOf(countDownInMinutes) + " Minute(s) till Shutdown");

                //text.setText("Finished");
                //TODO - set up turning off the phone

                Intent myIntent = new Intent(getApplicationContext(), CountDownActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                for (int i = 0; i < 10000; i++) {
                    startActivity(myIntent);
                }
            }

        };
        timer.start();
    }



    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }
    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
    }
    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destoryed", Toast.LENGTH_LONG).show();
        timer.cancel();
    }
}
