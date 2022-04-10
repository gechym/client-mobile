package com.example.dacs3.FirebaseCloudMessages

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.dacs3.R
import com.example.dacs3.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chanelId = "notification_id"
const val chanelName = "com.example.pushnotificationtfirebase"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("CheckLog","đang nhận thư")

        if (message.notification != null){
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)
        }
    }


    fun generateNotification(title : String , message : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)


        val bitmapLogo = BitmapFactory.decodeResource(resources, R.drawable.logo)
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, chanelId)
            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(bitmapLogo)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(chanelId, chanelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        Log.e("CheckLog","gửi thư rồi $title $message")
        notificationManager.notify(0,builder.build())


    }
}