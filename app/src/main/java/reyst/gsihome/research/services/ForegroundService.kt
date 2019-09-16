package reyst.gsihome.research.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {

    companion object {

        private const val ID = 200009
        private const val CHANNEL = "Notifications"

        var isForeground = false
        private set

    }

    private val binder = Binder()

    private fun checkAndStartForeground() {
        if (!isForeground) startForeground(ID, getNotification())
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "onRebind: ${intent?.getStringExtra("name")}")
        checkAndStartForeground()
        super.onRebind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ${intent?.extras}")

        checkAndStartForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.i(TAG, "onCreate")
        super.onCreate()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind: ${intent?.getStringExtra("name")}")
        return true //super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        stopForeground(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind: ${intent.getStringExtra("name")}")
        checkAndStartForeground()
        return binder
    }

    private fun getNotification(): Notification {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNeed()
        }

        val string = getString(R.string.app_name)
        return NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(string)
            .setContentText(string)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelIfNeed() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(CHANNEL) == null) {
            val channel = NotificationChannel(CHANNEL, "Notifications", NotificationManager.IMPORTANCE_LOW)
            channel.description = "Notifications"
            channel.enableLights(false)
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)
        }

    }


}
