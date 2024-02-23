package com.example.auto_app_open; 
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