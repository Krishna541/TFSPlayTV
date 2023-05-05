package `in`.fiberstory.tfsplaytv.presenter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import `in`.fiberstory.tfsplaytv.model.EpisodeDatumModel
import `in`.fiberstory.tfsplaytv.model.TvSeriesEpisode
import `in`.fiberstory.tfsplaytv.presenter.EpisodePresenter
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.appNotFound1
import `in`.fiberstory.tfsplaytv.utility.noActiveSubscription1
import `in`.fiberstory.tfsplaytv.utility.userLoginDialogAlert
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlexigoExpisodePresenter(activity: FragmentActivity, category: String) : Presenter() {
    private val TAG = EpisodePresenter::class.java.simpleName
    private val mContext: FragmentActivity
    private var mSelectedBackgroundColor = -1
    private var mDefaultBackgroundColor = -1
    private val mCategory: String

    init {
        mContext = activity
        mCategory = category
    }

    inner class ViewHolder(view: View) : Presenter.ViewHolder(view) {
        val episodePhoto: ImageView
        val providerImage: ImageView
        val episodeText: TextView
        val episodeParent: RelativeLayout
        var episodeDatumModel: TvSeriesEpisode? = null

        init {
            episodeParent = view.findViewById<View>(R.id.episodeParent) as RelativeLayout
            episodePhoto = view.findViewById<View>(R.id.episodePhoto) as ImageView
            providerImage = view.findViewById<View>(R.id.providerImage) as ImageView
            episodeText = view.findViewById<View>(R.id.episodeInfo) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        mDefaultBackgroundColor = ContextCompat.getColor(mContext, com.androijo.tvnavigation.R.color.black)
        mSelectedBackgroundColor = ContextCompat.getColor(mContext, com.androijo.tvnavigation.R.color.white)
        val mCardView: BaseCardView =
            object : BaseCardView(mContext, null, R.style.SideInfoCardStyle) {
                override fun setSelected(selected: Boolean) {
                    updateCardBackgroundColor(this, selected)
                    super.setSelected(selected)
                }
            }
        mCardView.isFocusable = true
        mCardView.addView(LayoutInflater.from(mContext).inflate(R.layout.item_episode, null))
        updateCardBackgroundColor(mCardView, false)
        return ViewHolder(mCardView)
    }

    private fun updateCardBackgroundColor(view: BaseCardView, selected: Boolean) {
        val color = if (selected) mSelectedBackgroundColor else mDefaultBackgroundColor
        view.setBackgroundColor(color)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        (viewHolder as ViewHolder).episodeDatumModel = item as TvSeriesEpisode
        val displayMetrics = DisplayMetrics()
        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels
        if (width == 1280 && height == 720) {
            (viewHolder as ViewHolder).episodeParent.layoutParams.width = 267
            (viewHolder as ViewHolder).episodePhoto.layoutParams.width = 267
            (viewHolder as ViewHolder).episodePhoto.layoutParams.height = 150
        } else {
            (viewHolder as ViewHolder).episodeParent.getLayoutParams().width = 400
            (viewHolder as ViewHolder).episodePhoto.layoutParams.width = 400
            (viewHolder as ViewHolder).episodePhoto.layoutParams.height = 225
        }
        (viewHolder as ViewHolder).episodeText.text =
             "E" + (viewHolder as ViewHolder).episodeDatumModel?.episodenumber.toString() + " | " + (viewHolder as ViewHolder).episodeDatumModel?.episodename
        val options: RequestOptions =
            RequestOptions().placeholder(R.drawable.ic_action_popular_channels_bg)
                .error(R.drawable.ic_action_popular_channels_bg).centerInside()
        (viewHolder as ViewHolder).providerImage.visibility = View.GONE
//        Glide.with(mContext).load((viewHolder as ViewHolder).episodeDatumModel?.service_logo_64)
//            .into((viewHolder as ViewHolder).providerImage)
//        if ((viewHolder as ViewHolder).episodeDatumModel?.id.equals("-1")) {
//            Glide.with(mContext).load(R.drawable.ic_action_more_landscape).apply(options)
//                .transition(GenericTransitionOptions.with(R.anim.fade_in))
//                .into((viewHolder as ViewHolder).episodePhoto)
//        } else {
            var url: String
            url = if ((viewHolder as ViewHolder).episodeDatumModel?.poster!!.isNotEmpty()) {
                (viewHolder as ViewHolder).episodeDatumModel?.poster!!
            } else {
                (viewHolder as ViewHolder).episodeDatumModel?.poster!!
            }
            Glide.with(mContext).load(url)
                .apply(options).transition(GenericTransitionOptions.with(R.anim.fade_in))
                .into((viewHolder as ViewHolder).episodePhoto)

//        }
        val baseCardView: BaseCardView = viewHolder.view as BaseCardView
//        baseCardView.setOnClickListener(View.OnClickListener {
//            val episodeDatumModel: TvSeriesEpisode? = (viewHolder as ViewHolder).episodeDatumModel
//            if (CheckUserLogin.loadPrefs(mContext) != 1) {
//                mContext.userLoginDialogAlert(mContext)
//            } else {
//                if (appInstalledOrNot(episodeDatumModel?.apkPackageId)) {
//                    if (MainActivity.isEpisodePlayable == 1) {
//                        MainActivity.launchAppUsingSSO(
//                            episodeDatumModel?.deeplink, episodeDatumModel?.ottServiceName
//                        )
//                    } else {
////                        noActiveSubscription(episodeDatumModel?.ottServiceName)
//                        mContext.noActiveSubscription1(mContext,episodeDatumModel?.ottServiceName)
//                    }
//                } else {
////                    appNotFound(episodeDatumModel?.ottServiceName)
//                    mContext.appNotFound1(mContext,episodeDatumModel?.ottServiceName)
//                }
//            }
//
//        })
    }

    fun appInstalledOrNot(uri: String?): Boolean {
        val pm: PackageManager = mContext.packageManager
        val app_installed: Boolean = try {
            if (uri != null) {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            }
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    override fun onViewAttachedToWindow(viewHolder: Presenter.ViewHolder) {
        // TO DO
    }
}