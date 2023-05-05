package `in`.fiberstory.tfsplaytv.fragments


import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.NewBannerCardView
import `in`.fiberstory.tfsplaytv.model.PromotionsItem
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * This Presenter will display a card consisting of an image on the left side of the card followed
 * by text on the right side. The image and text have equal width. The text will work like a info
 * box, thus it will be hidden if the parent row is inactive. This behavior is unique to this card
 * and requires a special focus handler.
 */
class BannerCardPresenter(context: Context, themeResID: Int) :
    AbstractCardPresenter<NewBannerCardView>(context, themeResID) {

    fun BannerCardPresenter(context: Context?, cardThemeResId: Int) {
        (ContextThemeWrapper(context, cardThemeResId))
    }

    fun bannerCardPresenter(context: Context) {
        (ContextThemeWrapper(context, R.style.GridCardStyle))
    }

    override fun onCreateView(): NewBannerCardView {
//        (ContextThemeWrapper(context, R.style.GridCardStyle))
        val cardView = NewBannerCardView(context)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return cardView
    }

//    override fun onCreateView(): NewBannerCardView {
//        val cardView = NewBannerCardView(context, R.style.GridCardStyle)
//        cardView.isFocusable = true
//        cardView.addView(LayoutInflater.from(context).inflate(R.layout.card_banner, null, false))
//        return cardView
//    }

    override fun onBindViewHolder(card: Any, cardView: NewBannerCardView) {
        val data = card as PromotionsItem

        cardView.setTitleText(data.promotionName)
        cardView.setMovieInfo(data.language_code + " . " + data.release_year)
        cardView.setSynopsis(data.synopsis)
//        cardView.setSynopsis("jdxcnjjncvvbhjvbcnbbvcbcvnbcv\nbhsdhbbbcbhbcbdbcbjfdbhjfdbfd\ncjdfhbjhbhjfdbhjdfbhdf")
        cardView.textAlignment =
            View.TEXT_ALIGNMENT_TEXT_START
        cardView.mainImageView?.let {
            Glide.with(context)
                .load(data.url) //.override(120,180)
                //.placeholder(R.drawable.placeholder)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(it)
        }
        cardView.mainImageView?.scaleType = ImageView.ScaleType.FIT_XY

        cardView.mainImageView?.setOnClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        }
//        val bannerImageview = cardView.findViewById<ImageView>(R.id.cardImage)
//        Glide.with(context)
//            .load(data.url)
//            .override(120, 180)
//            .listener(object : RequestListener<Drawable?> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any,
//                    target: Target<Drawable?>,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any,
//                    target: Target<Drawable?>,
//                    dataSource: DataSource,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//            })
//            .into(bannerImageview)
    }


}
