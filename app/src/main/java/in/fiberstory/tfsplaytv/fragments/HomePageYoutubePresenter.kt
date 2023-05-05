package `in`.fiberstory.tfsplaytv.fragments


import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.Item
import `in`.fiberstory.tfsplaytv.model.YoutubeImageCardView
import `in`.fiberstory.tfsplaytv.network.APIInterface
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
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
class HomePageYoutubePresenter(context: Context, themeResID: Int) :
    AbstractCardPresenter<YoutubeImageCardView>(context, themeResID) {

    override fun onCreateView(): YoutubeImageCardView {
        val cardView = YoutubeImageCardView(context, true)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return cardView
    }

    override fun onBindViewHolder(card: Any, cardView: YoutubeImageCardView) {
        val data = card as Item
        try {
            if (card.isMore.equals("-1", ignoreCase = false)) {
                cardView.ivViewMore?.visibility = View.VISIBLE
                cardView.mainImageView?.visibility = View.GONE
            } else {
                cardView.ivViewMore?.visibility = View.GONE
                cardView.mainImageView?.visibility = View.VISIBLE

                cardView.txtContentName?.visibility = View.VISIBLE
                cardView.txtContentName?.setText(data.snippet?.title)

                cardView.mainImageView?.let {
                    Glide.with(context)
                        .load(data.snippet!!.thumbnails?.medium?.url)
                        .placeholder(R.drawable.ic_action_popular_channels_bg)
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


                    val baseCardView = cardView
                    baseCardView.setOnClickListener {
                        val YoutubePLaylistIntent  = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(APIInterface.youtubeBaseURI + (data.snippet.resourceId?.videoId))
                        )
                        try {
                            context.startActivity(YoutubePLaylistIntent)
                        } catch (ex: ActivityNotFoundException) {
                            context.startActivity(YoutubePLaylistIntent)
                        }
                    }
                }

            }
        } catch (e: Exception) {
            Glide.with(context)
                .load(R.drawable.placeholder)
                .placeholder(R.drawable.ic_action_popular_channels_bg)
        }
        cardView.mainImageView?.scaleType = ImageView.ScaleType.FIT_XY
    }


}
