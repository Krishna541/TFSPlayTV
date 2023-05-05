package `in`.fiberstory.tfsplaytv.fragments


import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.HomeModel
import `in`.fiberstory.tfsplaytv.model.NewImageCardView
import android.content.Context
import android.graphics.drawable.Drawable
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
class HomePageCardPresenter(context: Context, themeResID: Int) :
    AbstractCardPresenter<NewImageCardView>(context, themeResID) {

    override fun onCreateView(): NewImageCardView {
        val cardView = NewImageCardView(context)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return cardView
    }

    override fun onBindViewHolder(card: Any, cardView: NewImageCardView) {

        val data = card as HomeModel

        cardView.providerImageView?.let {
            Glide.with(context)
                .load(card.serviceLogo64)
                .into(it)
        }
        if(card.content_type == "youtube"){
            cardView.mainImageView?.let {
                Glide.with(context)
                    .load(card.channelIcon)
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
            }
        }else if(card.content_type == "linear"){
            cardView.mainImageView?.let {
                Glide.with(context)
                    .load(card.channelIcon)
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
            }
        }
        else{
            cardView.mainImageView?.let {
                Glide.with(context)
                    .load(card.posterTv)
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
            }
        }
//        try {
//
//            cardView.mainImageView?.let {
//                Glide.with(context)
//                    .load(card.posterTv)
//                    .placeholder(R.drawable.ic_action_popular_channels_bg)
//                    .listener(object : RequestListener<Drawable?> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any,
//                            target: Target<Drawable?>,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            return false
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any,
//                            target: Target<Drawable?>,
//                            dataSource: DataSource,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            return false
//                        }
//                    })
//                    .into(it)
//            }
//        }catch (e : Exception){
//            Glide.with(context)
//                .load(R.drawable.placeholder)
//                .placeholder(R.drawable.ic_action_popular_channels_bg)
//
//        }
        cardView.mainImageView?.scaleType = ImageView.ScaleType.FIT_XY
    }


}
