package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.activities.MainActivity
import android.app.Application
import android.content.Context
import kotlin.jvm.Synchronized

class TFSApps : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener?) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {
        var mMain: MainActivity? = null
        var context: Context? = null

        @get:Synchronized
        var instance: TFSApps? = null
            private set

        fun getContext(): MainActivity? {
            return mMain
        }

        fun setContext(mContext: MainActivity?) {
            mMain = mContext
        }
    }
}