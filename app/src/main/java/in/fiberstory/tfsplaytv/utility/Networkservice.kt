package `in`.fiberstory.tfsplaytv.utility

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class Networkservice : Service() {
    var handler = Handler()
    override fun onCreate() {
        super.onCreate()
        handler.post(period)
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(period)
        isOn = false
    }

    fun isOnline(context: Context?): Boolean {
        InternetCheck().execute()
        return isOn
    }

    var period: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, (1 * 2000 /*- SystemClock.elapsedRealtime()%1000*/).toLong())
            Log.e("medha", "checking connection")
            val intent = Intent()
            intent.action = "checkinternet"
            intent.putExtra("online_status", "" + isOnline(this@Networkservice))
            LocalBroadcastManager.getInstance(this@Networkservice).sendBroadcast(intent)
        }
    }


    class InternetCheck :
        AsyncTask<Void?, Void?, Boolean?>() {
        // public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }
         override fun doInBackground(vararg voids: Void?): Boolean {
            return try {
                val sock = Socket()
                sock.connect(InetSocketAddress("8.8.8.8", 53), 3000)
                sock.close()
                Log.e("socket", "closed")
                true
            } catch (e: IOException) {
                Log.e("socket", e.message.toString())
                false
            }
        }

         override fun onPostExecute(internet: Boolean?) {
            isOn = internet!!
        }
    }

    companion object {
        var isOn = true
    }
}
