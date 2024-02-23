## ✨ Auto App Open
- This application is an example of automatically launching itself upon receiving a Firebase notification.

### Overview
- The AutoLaunch Example App showcases the seamless integration of Firebase notifications, enabling the application to initiate itself automatically in response to incoming notifications.

### Features
- Automatic Launch: Experience the convenience of the application launching itself effortlessly upon receiving a Firebase notification.

### Getting Started
- Follow these steps to get started with the AutoLaunch Example App:

### ❗You first need to properly configure the project with firebase.

## 🔧 Android Setup

### AndroidManifest.xml setup
Specify the below permission between the <manifest> tags
    
`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`

Add below lines in `<activity tag>`
```gradle
android:turnScreenOn="true"
android:showWhenLocked="true" 
```

Add below code snippet in AndroidManifest.xml below `</activity>` tag.

```xml
    <service
            android:name=".FirebaseMessageReceiver"
            android:directBootAware="true"
            android:stopWithTask="false"
            android:exported="true"
            android:showWhenLocked="true"
            android:turnScreenOn="true">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_channel_id"
            android:value="YOUR_APP_NAME" />
```

### Create FirebaseMessageReceiver.java file under 'android/app/src/main/java/com/example' and paste below code 

```java
package YOUR_PACKAGE_NAME;

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
```

### Paste below code in 'android/app/src/main/java/com/example/MainActivity.java' and if your project has kotlin then remove MainActivity code from kotlin file.,

```java
package YOUR_PACKAGE_NAME;

import android.app.NotificationChannel;
import android.app.NotificationManager; 
import android.os.Bundle;  
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine; 
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity  { 

    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
            NotificationChannel channel = new NotificationChannel("AutoOpenApp","AutoOpenAppappchanel", NotificationManager.IMPORTANCE_HIGH); 
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel); 
    }  
}
``` 


### Use [permission_handler](https://pub.dev/packages/permission_handler) package to access the System_Alert_Window permission.
    
   `await Permission.systemAlertWindow.request();`
   
- Make sure that your app has allowed `Appear on top` permission.


## Contributing
### We welcome contributions to enhance the functionality and improve the AutoLaunch Example App. Feel free to submit issues or pull requests.


## Authors

- [@NirmalsinhRathod](https://github.com/NirmalsinhRathod)

![Logo](https://cdn-ggkmd.nitrocdn.com/BzULJouLEmmtjCpJwHCmTIgakvECFbms/assets/images/optimized/rev-f1e70e0/www.creolestudios.com/wp-content/uploads/2021/07/cs-logo.svg)