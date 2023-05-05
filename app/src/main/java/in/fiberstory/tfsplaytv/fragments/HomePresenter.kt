package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.HomeModel
import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HomePresenter(activity: Activity, category: String) : Presenter() {
    private val TAG = HomePresenter::class.java.simpleName
    private val mContext: Activity
    private var mSelectedBackgroundColor = -1
    private var mDefaultBackgroundColor = -1
    private val mCategory: String

    inner class ViewHolder(view: View) : Presenter.ViewHolder(view) {
        val moviePhoto: ImageView
        val providerImage: ImageView
        val movieParent: RelativeLayout
        private var movieDatumModel: HomeModel? = null
        var movieList: HomeModel?
            get() = movieDatumModel
            set(mr) {
                movieDatumModel = mr
            }

        init {
            movieParent = view.findViewById<View>(R.id.movieParent) as RelativeLayout
            moviePhoto = view.findViewById<View>(R.id.moviePhoto) as ImageView
            providerImage = view.findViewById<View>(R.id.providerImage) as ImageView
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
        mCardView.setFocusable(true)
        mCardView.addView(LayoutInflater.from(mContext).inflate(R.layout.item_movie, null))
        updateCardBackgroundColor(mCardView, false)
        return ViewHolder(mCardView)
    }

    private fun updateCardBackgroundColor(view: BaseCardView, selected: Boolean) {
        val color = if (selected) mSelectedBackgroundColor else mDefaultBackgroundColor
        view.setBackgroundColor(color)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        (viewHolder as ViewHolder).movieList = item as HomeModel
        val displayMetrics = DisplayMetrics()
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels
        if (width == 1280 && height == 720) {
            (viewHolder as ViewHolder).movieParent.getLayoutParams().width = 267
            (viewHolder as ViewHolder).moviePhoto.layoutParams.width = 267
            (viewHolder as ViewHolder).moviePhoto.layoutParams.height = 150
        } else {
            (viewHolder as ViewHolder).movieParent.getLayoutParams().width = 400
            (viewHolder as ViewHolder).moviePhoto.layoutParams.width = 400
            (viewHolder as ViewHolder).moviePhoto.layoutParams.height = 225
        }
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_action_popular_channels_bg)
            .error(R.drawable.ic_action_popular_channels_bg)
            .centerInside()
        Glide.with(mContext)
            .load((viewHolder as ViewHolder).movieList?.serviceLogo64)
            .into((viewHolder as ViewHolder).providerImage)
        if ((viewHolder as ViewHolder).movieList?.isMore.equals("-1")) {
            Glide.with(mContext)
                .load(R.drawable.ic_action_more_landscape)
                .apply(options)
                .transition(GenericTransitionOptions.with(R.anim.fade_in))
                .into((viewHolder as ViewHolder).moviePhoto)
        } else {
            Glide.with(mContext)
                .load((viewHolder as ViewHolder).movieList?.posterTv)
                .apply(options)
                .transition(GenericTransitionOptions.with(R.anim.fade_in))
                .into((viewHolder as ViewHolder).moviePhoto)
        }

    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    override fun onViewAttachedToWindow(viewHolder: Presenter.ViewHolder) {
        // TO DO
    }

    init {
        mContext = activity
        mCategory = category
    }
}