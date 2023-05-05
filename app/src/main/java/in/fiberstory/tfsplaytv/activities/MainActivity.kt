package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.fragments.*
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.utility.ExitAppConfirmation
import `in`.fiberstory.tfsplaytv.utility.SonyAES256CBCEncryption
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.androijo.tvnavigation.NavigationMenu
import com.androijo.tvnavigation.interfaces.BackPressedStateChange
import com.androijo.tvnavigation.interfaces.FragmentChangeListener
import com.androijo.tvnavigation.interfaces.NavigationStateListener
import com.androijo.tvnavigation.utils.Constants
import com.androijo.tvnavigation.utils.Constants.backCount
import kotlinx.android.synthetic.main.activity_sample.*
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.time.Instant
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

class MainActivity : FragmentActivity(), NavigationStateListener, FragmentChangeListener,
    NavigationMenuCallback,BackPressedStateChange {

    companion object {

        // Login Preference
        fun savePrefs(key: String?, value: Int) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val edit = sp.edit()
            edit.putInt(key, value)
            edit.commit()
        }

        fun loadPrefs(): Int {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            return sp.getInt("pref_login", 0)
        }

        fun getDeviceID(): String? {
            return Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        }

        fun getDeviceName(): String? {
            return Build.BRAND + " - " + Build.MODEL
        }

        fun appInstalledOrNot(uri: String?): Boolean {
            Log.d("app", "appInstalledOrNot: "+uri)
            val pm = context.packageManager
            val app_installed: Boolean = try {
                if (uri != null && uri != "" ) {
                    pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                }
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return app_installed
        }

        fun noActiveSubscription(serviceName: String?) {
            val dialog = Dialog(context, R.style.DialogTheme)
            dialog.setContentView(R.layout.dialog_update)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.colorTransparent)))
            dialog.show()
            val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
            val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
            val dialogCancel = dialog.findViewById<Button>(R.id.dialogCancel)
            dialogCancel.visibility = View.GONE
            val dialogOk = dialog.findViewById<Button>(R.id.dialogOK)
            dialogTitle.text = "No Active Subscription!"
            dialogInfo.text =
                "You don't have an active " + serviceName + " premium subscription. " + "Visit the 'Offers' section of the TFS Play mobile app to subscribe to one of our packs"
            dialogOk.text = "OK"
            dialogOk.setOnClickListener { dialog.dismiss() }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun launchAppUsingSSO(deeplink: String?, serviceName: String?, appPackageName : String?) {
            Log.d("TFS_SSO_ROOT: ", "$deeplink | $serviceName")
            try {
                Log.d("TFS_SSO_TRY: ", "$deeplink | $serviceName")
                val sonyAES256CBCEncryption = SonyAES256CBCEncryption()
                var rootURL: String = ""
                if (serviceName?.toLowerCase()?.contains("sony") == true) {
                    val currentTime = Instant.now().toEpochMilli() + 5 * 60 * 1000
                    val paramValue: String =
                        (UserPreferences.getUserData(context).mobileNo) + currentTime
                    val token: String = sonyAES256CBCEncryption.encrypt(
                        paramValue, sonyAES256CBCEncryption.aesKey, sonyAES256CBCEncryption.iv
                    )
                    val decryptValue: String = sonyAES256CBCEncryption.decrypt(
                        token, sonyAES256CBCEncryption.aesKey, sonyAES256CBCEncryption.iv
                    )
                    rootURL = "$deeplink?source=aeon&loginType=sso&token=$token"
                    Log.d("TFS_SSO_: ", rootURL)
                } else {
                    if (deeplink != null) {
                        rootURL = deeplink
                    }
                }
                val sonyLiv = Intent()
                sonyLiv.action = Intent.ACTION_VIEW
                sonyLiv.data = Uri.parse(rootURL)
                if(rootURL.contains("https",true) && serviceName == "Hotstar"){
                    //com.amazon.amazonvideo.livingroom
                    try {
                        val packageIntent: Intent? = context.packageManager.getLaunchIntentForPackage(appPackageName!!)
                        if (packageIntent != null) {
                            context.startActivity(packageIntent)
                        }
                    }
                    catch ( e : Exception) {
                        e.printStackTrace()
                    }
                }else if (sonyLiv.resolveActivity(context.packageManager) != null) {
                    Log.d("TFS_SSO_IF: ", "$rootURL | $deeplink | $serviceName")
                    context.startActivity(sonyLiv)
                } else {
                    Log.d("TFS_SSO_ELSE: ", "$rootURL | $deeplink | $serviceName")
                    appNotFound(serviceName)
                }
            } catch (e: InvalidKeyException) {
                Log.d("TFS_SSO_EXCEPTION: ", e.message.toString())
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                Log.d("TFS_SSO_EXCEPTION: ", e.message.toString())
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                Log.d("TFS_SSO_EXCEPTION: ", e.message.toString())
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                Log.d("TFS_SSO_EXCEPTION: ", e.message.toString())
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                Log.d("TFS_SSO_EXCEPTION: ", e.message.toString())
                e.printStackTrace()
            }
        }



        fun appNotFound(serviceName: String?) {
            val dialog = Dialog(context, R.style.DialogTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_update)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.colorTransparent)))
            dialog.show()
            val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
            val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
            val dialogOK = dialog.findViewById<TextView>(R.id.dialogOK)
            val dialogCancel = dialog.findViewById<TextView>(R.id.dialogCancel)
            dialogCancel.visibility = View.GONE
            dialogOK.text = "Close"
            dialogTitle.text = "App not found!"
            dialogInfo.text =
                "Please install " + serviceName + " application to view this content. Please install the application from home page."
            dialogOK.setOnClickListener {
                dialog.dismiss()
//                val intent = Intent(context, MainActivity::class.java)
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity)
//                    .toBundle()
//                context.startActivity(intent, bundle)
//                (context as Activity).finish()
            }
        }

        lateinit var context: Context
        var isMoviePlayable = 0
        var isTVShowsPlayable = 0
        var isLiveTVPlayable = 0
        var isEpisodePlayable: Int? = 0
        var mPlayable: Int? = 0
        private var _key: ByteArray? = null
        private var _iv: kotlin.ByteArray? = null
        private var _cx: Cipher? = null


    }

    private lateinit var searchFragment: SearchBarFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var showsFragment: ShowsFragment
    private lateinit var movieFragment: MoviesFragment
    private lateinit var onRentMovieFragment : OnRentMovieFragment
    private lateinit var liveTVFragment: LiveTVFragment
    private lateinit var tvShowsFragment: TVShowsFragment
    private lateinit var documentariesFragment: DocumentariesFragment
    private lateinit var shortFilmFragment: ShortFilmFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var navMenuFragment: NavigationMenu
    private var currentSelectedFragment = Constants.nav_menu_home
    private var isStackEmpty = false
    private var isNavExpand = false
    var settingFirstFocus =  1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        context = this@MainActivity
        navMenuFragment = NavigationMenu()
        fragmentReplacer(nav_fragment.id, navMenuFragment)
        homeFragment = HomeFragment()
        fragmentReplacer(main_FL.id, homeFragment)
    }

    private fun fragmentReplacer(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }

    /**
     * communication from left-side navigation to right-side content
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStateChanged(expanded: Boolean, lastSelected: String?) {
        isNavExpand = expanded
        if (!expanded) {
            nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
            nav_fragment.clearFocus()

            if(lastSelected == "Settings") {
                if (settingsFragment.getFlag()){

                    settingsFragment.restoreFocusOnHelpOkBtn()

                }
                else {
                    settingsFragment.restoreFocusOnPrivacy()
                }
            }

            if(lastSelected == "Profile") {
                profileFragment.restoreDrawableFocusOnLogout()
            }

            when (currentSelectedFragment) {
                Constants.nav_menu_search -> {
                    currentSelectedFragment = Constants.nav_menu_search
//                    searchFragment.selectFirstItem()
                }
                Constants.nav_menu_profile -> {
                    currentSelectedFragment = Constants.nav_menu_profile
//                    profileFragment.selectFirstItem()
                }
                Constants.nav_menu_home -> {
                    currentSelectedFragment = Constants.nav_menu_home
                    homeFragment.restoreSelection()
                }
                Constants.nav_menu_movie -> {
                    currentSelectedFragment = Constants.nav_menu_movie
//                    movieFragment.selectFirstItem()
                }
                Constants.nav_menu_on_rent_movie -> {
                    currentSelectedFragment = Constants.nav_menu_movie
                }
                Constants.nav_menu_tv_shows -> {
                    currentSelectedFragment = Constants.nav_menu_tv_shows
//                    tvShowsFragment.selectFirstItem()
                }
                Constants.nav_menu_liveTV -> {
                    currentSelectedFragment = Constants.nav_menu_liveTV
                    liveTVFragment.selectFirstItem()
                }
                Constants.nav_menu_documentaries -> {
                    currentSelectedFragment = Constants.nav_menu_documentaries
//                    documentariesFragment.selectFirstItem()
                }
                Constants.nav_menu_short_films -> {
                    currentSelectedFragment = Constants.nav_menu_short_films
//                    shortFilmFragment.selectFirstItem()
                }
                Constants.nav_menu_settings -> {
                    currentSelectedFragment = Constants.nav_menu_settings
//                    settingsFragment.selectFirstItem()
                }
            }
        } else {
            //do
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun switchFragment(fragmentName: String?) {
        nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
        isStackEmpty = true
        when (fragmentName) {
            Constants.nav_menu_search -> {
                searchFragment = SearchBarFragment()
                fragmentReplacer(main_FL.id, searchFragment)
//                searchFragment.selectFirstItem()
            }
            Constants.nav_menu_profile -> {
                profileFragment = ProfileFragment()
                fragmentReplacer(main_FL.id, profileFragment)
//                profileFragment.selectFirstItem()
            }
            Constants.nav_menu_home -> {
                homeFragment = HomeFragment()
                fragmentReplacer(main_FL.id, HomeFragment())
                homeFragment.restoreSelection()
            }
            Constants.nav_menu_tv_shows -> {
                tvShowsFragment = TVShowsFragment()
                fragmentReplacer(main_FL.id, tvShowsFragment)
//                tvShowsFragment.selectFirstItem()
            }
            Constants.nav_menu_movie -> {
                movieFragment = MoviesFragment()
                fragmentReplacer(main_FL.id, movieFragment)
//                movieFragment.selectFirstItem()
            }
            Constants.nav_menu_on_rent_movie -> {
                onRentMovieFragment = OnRentMovieFragment()
                fragmentReplacer(main_FL.id, onRentMovieFragment)
            }
            Constants.nav_menu_shows -> {
                showsFragment = ShowsFragment()
                fragmentReplacer(main_FL.id, showsFragment)
                showsFragment.selectFirstItem()
            }
            Constants.nav_menu_documentaries -> {
                documentariesFragment = DocumentariesFragment()
                fragmentReplacer(main_FL.id, documentariesFragment)
//                documentariesFragment.selectFirstItem()
            }
            Constants.nav_menu_short_films -> {
                shortFilmFragment = ShortFilmFragment()
                fragmentReplacer(main_FL.id, shortFilmFragment)
//                shortFilmFragment.selectFirstItem()
            }
            Constants.nav_menu_settings -> {
                settingsFragment = SettingsFragment()
                fragmentReplacer(main_FL.id, settingsFragment)
//                settingsFragment.selectFirstItem()
            }
            Constants.nav_menu_liveTV -> {
                liveTVFragment = LiveTVFragment()
                fragmentReplacer(main_FL.id, liveTVFragment)
                liveTVFragment.selectFirstItem()
            }
        }
    }

    override fun navMenuToggle(toShow: Boolean) {

        try {
            if (toShow) {
                nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_open)
                main_FL.clearFocus()
                nav_fragment.requestFocus()
                navEnterAnimation()
                navMenuFragment.openNav()
            } else {
                nav_fragment.setBackgroundResource(R.drawable.ic_nav_bg_closed)
                nav_fragment.clearFocus()
                main_FL.requestFocus()
                navMenuFragment.closeNav()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
//            KeyEvent.KEYCODE_BACK -> {
//                backCount++
//                if (isStackEmpty && isNavExpand) {
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//
//                    startActivity(intent)
//                    backCount = 0
//                    isStackEmpty = false
//                } else {
//                    if(isNavExpand && backCount == 2){
//                        ExitAppConfirmation(this@MainActivity) {
//                            finish()
//                        }
//                    }
//                    if (backCount == 1) {
//                        navMenuToggle(true)
//
//                    } else {
//                        ExitAppConfirmation(this@MainActivity) {
//                            finish()
//                        }
//                    }

//                }
//                navMenuToggle(true)
//            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                val fragment_left = supportFragmentManager.findFragmentById(R.id.main_FL)
//                if (fragment_left is HomeFragment) {
//                    navMenuFragment.openNav()
//                }
//                val fragment_left = supportFragmentManager.findFragmentById(R.id.main_FL)
//                if (fragment_left is HomeFragment) {
//                    navMenuToggle(true)
//                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                val fragment_down = supportFragmentManager.findFragmentById(R.id.main_FL)
            }
        }
        return false

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttachFragment(fragment: Fragment) {

        when (fragment) {
            is ProfileFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is HomeFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is NewsFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is MoviesFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is DocumentariesFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is ShortFilmFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is SettingsFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is LiveTVFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is TVShowsFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is SearchBarFragment -> {
                fragment.setNavigationMenuCallback(this)

            }
            is OnRentMovieFragment -> {
                fragment.setNavigationMenuCallback(this)
            }
            is NavigationMenu -> {
                fragment.setFragmentChangeListener(this)
                fragment.setNavigationStateListener(this)
                fragment.setBackPressedListener(this)
            }
        }
    }


    private fun navEnterAnimation() {
        val animate = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        nav_fragment.startAnimation(animate)
    }

    override fun onBackPressed() {
        super.finish()
    }

    //2
    override fun backPressedStateChange() {
        if (isStackEmpty) {
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            startActivity(intent)
            backCount = 0
            isStackEmpty = false
        }else{
            if(isNavExpand){
                    finish()

            }
        }
    }

}
