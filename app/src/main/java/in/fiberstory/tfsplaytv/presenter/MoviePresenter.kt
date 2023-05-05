package `in`.fiberstory.tfsplaytv.presenter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.CommanActivity
import `in`.fiberstory.tfsplaytv.activities.DetailsActivity
import `in`.fiberstory.tfsplaytv.fragments.AbstractCardPresenter
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.model.NewDocumenteryImageCardView
import `in`.fiberstory.tfsplaytv.utility.GlideApp
import android.app.Activity
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_popular.view.*

class MoviePresenter(private val mContext: Activity, private val mCategory: String) :
    AbstractCardPresenter<NewDocumenteryImageCardView>(mContext) {
    private val TAG = MoviePresenter::class.java.simpleName
    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    override fun onViewAttachedToWindow(viewHolder: Presenter.ViewHolder) {
        // TO DO
    }

    override fun onCreateView(): NewDocumenteryImageCardView {
        val cardView = NewDocumenteryImageCardView(context, "Short Films")
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return cardView
    }

    override fun onBindViewHolder(card: Any, cardView: NewDocumenteryImageCardView) {
        val data = card as DocumentaryItemsModel

        val displayMetrics = DisplayMetrics()
        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        if (width == 1280 && height == 720) {
            cardView.layParent?.layoutParams?.width = 180
            cardView.layParent?.layoutParams?.height = 250
            cardView.mainImageView?.layoutParams?.width = 180
            cardView.mainImageView?.layoutParams?.height = 183

        } else {
            cardView.layParent?.layoutParams?.width = 195
            cardView.mainImageView?.layoutParams?.width = 195
            cardView.mainImageView?.layoutParams?.height = 275
        }

        if (!TextUtils.isEmpty(card.free) && card.free == "0") {
            cardView.imgFreeContentView?.visibility = View.VISIBLE
        } else {
            cardView.imgFreeContentView?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(card.movieName)) {
            if (card.movieName?.length!! > 14) {
                cardView.content_name?.visibility = View.VISIBLE
                cardView.content_name.text = card.movieName.substring(0, 13) + "..."
            } else {
                cardView.content_name.text = card.movieName
                cardView.content_name?.visibility = View.VISIBLE
            }
        } else {
            cardView.content_name?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(card.releaseYear)) {
            cardView.txtContentYear?.text = card.releaseYear
            cardView.txtContentYear?.visibility = View.VISIBLE
        } else {
            cardView.txtContentYear?.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(card.languageCode)) {
            cardView.txtLanguage?.visibility = View.VISIBLE
            cardView.txtLanguage?.text = card.languageCode
        } else {
            cardView.txtLanguage?.visibility = View.GONE
        }


        cardView.providerImageView?.let {
            Glide.with(context)
                .load(card.serviceLogo64)
                .into(it)

            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_action_popular_channels_bg)
                .error(R.drawable.ic_action_popular_channels_bg)
                .centerInside()

            if (card.isMore.equals("-1", ignoreCase = true)) {
                cardView.ivLoadMore?.visibility = View.VISIBLE
                cardView.mainImageView?.visibility = View.GONE
                Log.d(TAG, "onBindViewHolder: more item added" + card.isMore)
//                cardView.mainImageView?.let { it1 ->
//                    Glide.with(mContext)
//                        .load(R.drawable.ic_action_more_portrait)
//                        .apply(options)
//                        .transition(GenericTransitionOptions.with<Drawable>(R.anim.fade_in))
//                        .into(it1)
//                }
            } else {

                cardView.ivLoadMore?.visibility = View.GONE
                cardView.mainImageView?.visibility = View.VISIBLE
                cardView.mainImageView?.let { it1 ->
                    GlideApp.with(mContext)
                        .load(card.posterMobile)
                        .apply(options)
                        .transition(GenericTransitionOptions.with<Drawable>(R.anim.fade_in))
                        .into(it1)
                    cardView.mainImageView?.scaleType = ImageView.ScaleType.FIT_XY

                }
            }

            val baseCardView = cardView
            baseCardView.setOnClickListener {
                if (card.isMore.equals("-1")
                ) {
                    mContext.startActivity(CommanActivity.createIntentMovie(mContext,
                        mCategory, 0))
                } else {
                    mContext.startActivity(
                        DetailsActivity.createIntentMovie(
                            mContext,
                            card
                        )
                    )
                }
            }

        }
    }
}