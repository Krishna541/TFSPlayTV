package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.fragments.CommanFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity

class CommanActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        if (savedInstanceState == null) {
            val fragment = CommanFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
    }

    fun onNetworkConnectionChanged(isConnected: Boolean) {
        // Do nothing ...
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        fun createIntentMovie(context: Context?, category: String?, mode: Int): Intent {
            val intent = Intent(context, CommanActivity::class.java)
            intent.putExtra("CATEGORY", category)
            intent.putExtra("MODE", mode)
            return intent
        }
        fun createOnRentIntent(context: Context?, category: String?, mode: Int , channelId : Int , userId : Int):Intent{
            val intent = Intent(context, CommanActivity::class.java)
            intent.putExtra("CATEGORY", category)
            intent.putExtra("MODE", mode)
            intent.putExtra("ChannelId" , channelId)
            intent.putExtra("PlexigoUserId" , userId)
            return intent
        }

        fun youtubePlayListId(context: Context?, category: String?, playlistId: String?, mode: Int): Intent {
            val intent = Intent(context, CommanActivity::class.java)
            intent.putExtra("CATEGORY", category)
            intent.putExtra("PLAYLISTID", playlistId)
            intent.putExtra("MODE", mode)
            return intent

        }

    }
}