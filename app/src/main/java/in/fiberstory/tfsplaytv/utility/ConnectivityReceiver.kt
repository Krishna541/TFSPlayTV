package `in`.fiberstory.tfsplaytv.utility

import android.content.BroadcastReceiver
import android.content.Intent
import android.net.ConnectivityManager
import android.content.Context
import android.net.NetworkInfo

class ConnectivityReceiver : BroadcastReceiver() {
    private val isConnected = false
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork!!.isConnectedOrConnecting
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        var activeNetwork: NetworkInfo? = null
        var TAG = ConnectivityReceiver::class.java.simpleName
        private const val LOG_TAG = "NetworkChangeReceiver"
        fun isConnected(): Boolean {
            val cm = TFSApps.instance?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork!!.isConnected
        }
    }
}