package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.appInstalledOrNot
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isEpisodePlayable
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.launchAppUsingSSO
import `in`.fiberstory.tfsplaytv.fragments.EpisodeFragment
import `in`.fiberstory.tfsplaytv.model.EpisodeDatumModel
import `in`.fiberstory.tfsplaytv.model.EpisodeModel
import `in`.fiberstory.tfsplaytv.utility.*
import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.widget.TextView
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.WindowManager
import android.util.DisplayMetrics
import android.graphics.Bitmap
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class EpisodeActivity : FragmentActivity(), EpisodeFragment.OnBrowseRowListener,
    ConnectivityReceiver.ConnectivityReceiverListener {
    private var btnWatchNow: Button? = null
    private var mMainFrame: View? = null
    private var mContentPanel: View? = null
    private var mBackgroundWithPreview: Drawable? = null
    private var mContentImage: ImageView? = null
    private var content_type: TextView? = null
    private var content_name: TextView? = null
    private var content_other: TextView? = null
    private var content_info: ExpandableTextView? = null

    //private var content_info: TextView? = null
    private var bundle: Bundle? = null
    private var episodeDatumModel: EpisodeDatumModel? = null
    private var content_details: LinearLayout? = null

    //private var content_details: LinearLayout? = null
    private  var content_expand: TextView? = null
//    private  var content_expand: TextView? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        Init()
        setData()
        if (savedInstanceState == null) {
            val mBrowseFragment = EpisodeFragment()
            supportFragmentManager.beginTransaction().add(R.id.contentPanel, mBrowseFragment)
                .commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        if (episodeDatumModel != null) {

            if (episodeDatumModel is EpisodeDatumModel) {
                if (episodeDatumModel?.episodeInfo?.length!! > 30) {


                    content_expand!!.visibility = View.VISIBLE
                } else {

                    content_expand!!.visibility = View.GONE
                }
            }
        }


        bundle = intent.extras
        Handler().postDelayed({
            btnWatchNow!!.isFocusable = true
            btnWatchNow!!.isFocusableInTouchMode = true
            btnWatchNow!!.requestFocus()
        }, 1000)

        btnWatchNow!!.setOnClickListener {
            if (CheckUserLogin.loadPrefs(this) != 1) {
                userLoginDialogAlert(this)
            }
            else {
                if (CheckUserLogin.loadPrefs(this) == 1) {
                    if (episodeDatumModel != null) {
                        if (episodeDatumModel is EpisodeDatumModel) {
                            if (appInstalledOrNot(episodeDatumModel?.apkPackageId)) {
                                if (isEpisodePlayable == 1) {
                                    //launchApp(episodeDatumModel.getIsLeanback(), episodeDatumModel.getDeeplink_url_tv(), episodeDatumModel.getApkPackageId());
                                    launchAppUsingSSO(
                                        episodeDatumModel?.deeplink_url_tv,
                                        episodeDatumModel?.ottServiceName,
                                        episodeDatumModel?.apkPackageId
                                    )
                                } else {
//                                    noActiveSubscription(episodeDatumModel?.ottServiceName)
                                    this@EpisodeActivity.noActiveSubscription1(this@EpisodeActivity,episodeDatumModel?.ottServiceName)
                                }
                            } else {
//                                appNotFound(episodeDatumModel?.ottServiceName)
                                this@EpisodeActivity.appNotFound1(this@EpisodeActivity,episodeDatumModel?.ottServiceName)
                            }
                        }
                    }
                }
                else {
                    startActivity(
                        ProfileActivity.createIntentShow(
                            this
                        )
                    )
                }
            }
        }

        content_expand!!.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
            if (b) {
                content_expand!!.setTextColor(ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.orange))
            } else {
                content_expand!!.setTextColor(ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.white))
            }
        })


        content_expand!!.setOnClickListener {
            if (content_info!!.isExpanded) {
                content_info!!.collapse()
                content_expand!!.text = getString(R.string.content_more)
            } else {
                content_info!!.expand()
                content_expand!!.text = getString(R.string.content_less)
            }
        }

    }

    fun stringDecode(textView: TextView?, s: String?) {
        try {
            textView!!.text =
                URLDecoder.decode(s!!.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25"), "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private fun Init() {
        btnWatchNow = findViewById<View>(R.id.btnWatchNow) as Button
        Handler().postDelayed({
            btnWatchNow!!.isFocusable = true
            btnWatchNow!!.isFocusableInTouchMode = true
            btnWatchNow!!.requestFocus()
        }, 1000)

//        content_expand = findViewById<View>(R.id.expand) as TextView
        content_expand = findViewById<View>(R.id.expand) as TextView
//        content_expand = findViewById(R.id.expand)

        mContentImage = findViewById<View>(R.id.content_image) as ImageView
        content_type = findViewById(R.id.content_type)
        content_name = findViewById(R.id.content_name)
        content_other = findViewById(R.id.content_other)
        content_info = findViewById(R.id.content_info)
        content_details = findViewById(R.id.content_details)


        val display = windowManager.defaultDisplay
        val windowSize = Point()
        display.getSize(windowSize)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val imageWidth: Int
        val imageHeight: Int
        if (width == 1280 && height == 720) {
            imageWidth = resources.getDimension(R.dimen.content_image_width_small).toInt()
            imageHeight = resources.getDimension(R.dimen.content_image_height_small).toInt()
            content_details?.layoutParams?.width = 450
            content_details?.layoutParams?.height = 400
        } else {
            imageWidth = resources.getDimension(R.dimen.content_image_width).toInt()
            imageHeight = resources.getDimension(R.dimen.content_image_height).toInt()
            content_details?.layoutParams?.width = 750
            content_details?.layoutParams?.height = 600
        }
        mContentImage!!.layoutParams.height = imageHeight
        mContentImage!!.layoutParams.width = imageWidth
        val gradientSize = resources.getDimension(R.dimen.content_image_gradient_size).toInt()
        // Create the background
        val background: Bitmap = BackgroundImageUtils.createBackgroundWithPreviewWindow(
            windowSize.x,
            windowSize.y,
            imageWidth,
            imageHeight,
            gradientSize,
            ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.black)
        )
        mBackgroundWithPreview = BitmapDrawable(resources, background)
        // Set the background
        mMainFrame = findViewById(R.id.main_frame)
        mContentPanel = findViewById(R.id.contentPanel)


        mMainFrame?.background = mBackgroundWithPreview



    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

    }

    companion object {
        private const val CONTENT_IMAGE_CROSS_FADE_DURATION = 1500
        fun createIntentShow(context: Context?, showID: String?, showName: String?): Intent {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtra("TVSHOWS", showID)
            intent.putExtra("TVSHOWS_NAME", showName)
            return intent
        }
    }

    override fun onItemSelected(item: Any?, index: Long) {
        if (item is EpisodeDatumModel) {
            episodeDatumModel = item as EpisodeDatumModel
            val episodeDatumModel: EpisodeDatumModel = item as EpisodeDatumModel
            if (bundle != null) stringDecode(content_type, bundle!!.getString("TVSHOWS_NAME"))
            stringDecode(content_info, episodeDatumModel.episodeInfo)
            stringDecode(content_name, episodeDatumModel.episodeTitle)

            if (episodeDatumModel?.episodeInfo?.length!! > 80) {
                content_expand!!.visibility = View.VISIBLE
            } else {

                content_expand!!.visibility = View.GONE
            }




            val timeline: Int? = episodeDatumModel?.duration?.toInt()
            val hours = timeline?.div(3600)
            val minutes = (timeline?.rem(3600))?.div(60)
            val time = hours.toString() + "h " + minutes + "m"
            content_other?.text =
                episodeDatumModel.censorRating + " | " + time + " | S" + episodeDatumModel.seasonNo + " E" + episodeDatumModel.episodeNo
            if (!episodeDatumModel.posterTv.equals("")) {
                GlideHelper.loadImageWithCrossFadeTransition(
                    this@EpisodeActivity,
                    mContentImage,
                    episodeDatumModel.posterTv,
                    CONTENT_IMAGE_CROSS_FADE_DURATION,
                    R.drawable.ic_action_popular_channels_bg
                )
            } else if (!episodeDatumModel.episodePoster.equals("")) {
                GlideHelper.loadImageWithCrossFadeTransition(
                    this@EpisodeActivity,
                    mContentImage,
                    episodeDatumModel.episodePoster,
                    CONTENT_IMAGE_CROSS_FADE_DURATION,
                    R.drawable.ic_action_popular_channels_bg
                )
            }
            if (episodeDatumModel.posterTv != null && episodeDatumModel.posterTv?.isNotEmpty() == true || episodeDatumModel.episodePoster != null && episodeDatumModel.episodePoster!!.isNotEmpty()) {
                mMainFrame!!.background = mBackgroundWithPreview
            } else {
                mMainFrame!!.setBackgroundColor(Color.TRANSPARENT)
            }
        }	else if(item is EpisodeModel){
            val episodeModel: EpisodeModel = item as EpisodeModel
            content_name!!.visibility = View.GONE
            if (bundle != null) stringDecode(content_type, bundle!!.getString("TVSHOWS_NAME"))
            stringDecode(content_info, episodeModel.show_info)

            if (episodeModel.show_info?.length!! > 30) {


                content_expand!!.visibility = View.VISIBLE
            } else {

                content_expand!!.visibility = View.GONE
            }

            content_other?.text =
                episodeModel.language_code + " | "  + episodeModel.censor_rating + " | " + episodeModel.release_year
            if (!episodeModel.poster_tv.equals("")) {
                GlideHelper.loadImageWithCrossFadeTransition(
                    this@EpisodeActivity,
                    mContentImage,
                    episodeModel.poster_tv,
                    CONTENT_IMAGE_CROSS_FADE_DURATION,
                    R.drawable.ic_action_popular_channels_bg
                )
            } else if (!episodeModel.poster_tv.equals("")) {
                GlideHelper.loadImageWithCrossFadeTransition(
                    this@EpisodeActivity,
                    mContentImage,
                    episodeModel.poster_tv,
                    CONTENT_IMAGE_CROSS_FADE_DURATION,
                    R.drawable.ic_action_popular_channels_bg
                )
            }
            if (episodeModel.poster_tv != null && episodeModel.poster_tv?.isNotEmpty() == true) {
                mMainFrame!!.background = mBackgroundWithPreview
            } else {
                mMainFrame!!.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}