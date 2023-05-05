package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class AddWatchHistoryService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //    String TAG = AddWatchHistoryService.this.getClass().getSimpleName() + "fatal";
    var TAG = "ganeshdada"
    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "insidecreated")
        // do stuff like register for BroadcastReceiver, etc.

        // Create the Foreground Service
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(
                notificationManager
            ) else ""
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager): String {
        val channelId = "my_service_channelid"
        val channelName = "My Foreground Service"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        // omitted the LED color
        channel.importance = NotificationManager.IMPORTANCE_NONE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    override fun onStartCommand(data: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "insidestartcommand")
        try {
            if (data != null && data.hasExtra("playback")) {
                val startPosition = data.getIntExtra("playback", 0)
                val contentId = data.getIntExtra("contentId", 0)
                Log.e(TAG, "startPosition $startPosition")
                Log.e(TAG, "contentId $contentId")
                //addWatchHistory(startPosition, contentId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return START_STICKY
    }

//    fun addWatchHistory(duration: Int, contentId: Int) {
//        Log.e(TAG, "insideapi")
//        try {
//            val jsonBody = JSONObject()
//            jsonBody.put("UserId", Integer.valueOf(AppConstants.UserId))
//            jsonBody.put("ContentId", contentId)
//            jsonBody.put("WatchDuration", duration)
//            val request: JsonObjectRequest = object : JsonObjectRequest(
//                Request.Method.POST,
//                WsConstants.DOMAIN_URL + WsConstants.AddWatchHistory,
//                jsonBody,
//                object : Listener<JSONObject?>() {
//                    fun onResponse(response: JSONObject?) {
//                        if (response != null) {
//                            try {
//                                if (response.has("statusCode") && response.getString("statusCode") == "200") {
//                                    //success
//                                    Log.e(TAG, " success")
//                                    stopSelf()
//                                } else {
//                                    if (response.has("status") && response.getJSONObject("status") != null) {
//                                        Toast.makeText(
//                                            this@AddWatchHistoryService,
//                                            "" + response.getString("status"),
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        Log.e(TAG, response.getString("status"))
//                                        stopSelf()
//                                    }
//                                }
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                                stopSelf()
//                            }
//                        }
//                    }
//                },
//                object : ErrorListener() {
//                    fun onErrorResponse(error: VolleyError) {
//                        if (error is TimeoutError) {
//                            Toast.makeText(
//                                this@AddWatchHistoryService,
//                                R.string.timeout_message,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            stopSelf()
//                            return
//                        }
//                        if (error.getCause() is IOException) {
//                            Toast.makeText(
//                                this@AddWatchHistoryService,
//                                getString(R.string.internet_not_available),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            stopSelf()
//                            return
//                        }
//                        Toast.makeText(
//                            this@AddWatchHistoryService,
//                            "" + error.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        stopSelf()
//                    }
//                }) {
//                //This is for Headers If You Needed
//                @get:Throws(AuthFailureError::class)
//                val headers: Map<String, String>
//                    get() {
//                        val params: MutableMap<String, String> = HashMap()
//                        params["Content-Type"] = "application/json"
//                        params["X-API-Key"] = WsConstants.xApiKey
//                        return params
//                    }
//            }
//            val policy: RetryPolicy = DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//            request.setRetryPolicy(policy)
//            queue.add(request)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//            stopSelf()
//        }
//    }
}
