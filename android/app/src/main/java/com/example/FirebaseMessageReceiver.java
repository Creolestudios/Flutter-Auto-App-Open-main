package com.example.auto_app_open;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build; 
import android.util.Log;
import android.widget.Toast; 
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage; 
import java.util.List;
import java.util.concurrent.ExecutionException; 

public class FirebaseMessageReceiver
        extends FirebaseMessagingService { 
    private static final String TAG = FirebaseMessageReceiver.class.getSimpleName();
    boolean foreground = false; 
    @SuppressLint("WrongThread")
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage == null) {
            return;
        }
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody()); 
               try {
                foreground = new ForegroundCheckTask().execute(this).get();
                if (!foreground) {
                    Log.d("foreground", foreground+"");
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK); 
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE);
                    pendingIntent.cancel();
                    startActivity(i); 
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } 
        } 
    }

    public void handleIntent(Intent intent)
    {
        try {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("FirebaseMessageReceiver");
                for (String key : intent.getExtras().keySet()){
                    builder.addData(key, intent.getExtras().get(key).toString());
                } 
                onMessageReceived(builder.build());
            }else{
                super.handleIntent(intent);
            }
            Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_SHORT).show(); 
        }
        catch (Exception e){
            super.handleIntent(intent);
        }
    }
  
    public class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    } 
}