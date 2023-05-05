package `in`.fiberstory.tfsplaytv.presenter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.fragments.AbstractCardPresenter
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.model.NewDocumenteryImageCardView
import android.app.Activity
import androidx.leanback.widget.Presenter
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.bumptech.glide.GenericTransitionOptions
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_popular.view.*

class DocumentariesPresenter(private val mContext: Activity) :
    AbstractCardPresenter<NewDocumenteryImageCardView>(mContext) {
    private val TAG = DocumentariesPresenter::class.java.simpleName

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    override fun onViewAttachedToWindow(viewHolder: Presenter.ViewHolder) {
        // TO DO
    }

    override fun onCreateView(): NewDocumenteryImageCardView {
        val cardView = NewDocumenteryImageCardView(context, "Documentaries")
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
//        if (width == 1280 && height == 720) {
//            cardView.layParent.layoutParams.width = 267
//            cardView.ivPoster.layoutParams.width = 267
//            cardView.ivPoster.layoutParams.height = 150
//        } else {
//            cardView.layParent.layoutParams.width = 400
//            cardView.ivPoster.layoutParams.width = 400
//            cardView.ivPoster.layoutParams.height = 225
//        }

        if (!TextUtils.isEmpty(card.free) && card.free == "0") {
            cardView.imgFreeContentView?.visibility = View.VISIBLE
        } else {
            cardView.imgFreeContentView?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(card.movieName)) {
            cardView.content_name.text = card.movieName
            cardView.content_name?.visibility = View.VISIBLE
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

            cardView.mainImageView?.let { it1 ->
                Glide.with(mContext)
                    .load(card.posterTv)
                    .placeholder(R.drawable.ic_action_popular_channels_bg)
                    .transition(GenericTransitionOptions.with<Drawable>(R.anim.fade_in))
                    .into(it1)
                cardView.mainImageView?.scaleType = ImageView.ScaleType.FIT_XY

            }
        }
    }
}