package `in`.fiberstory.tfsplaytv.model

import `in`.fiberstory.tfsplaytv.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.leanback.widget.BaseCardView


open class YoutubeImageCardView(context: Context?, isViewMore: Boolean) : BaseCardView(context) {
    var mainImageView: ImageView? = null
    var ivViewMore: ImageView? = null
    var channelImageview: ImageView? = null
    var providerImageView: ImageView? = null
    var imgFreeContentView: ImageView? = null
    var txtContentName: TextView? = null
    var txtContentLanguage: TextView? = null
    var txtContentYear: TextView? = null
    var card: CardView? = null
    var layShadow: LinearLayout? = null
    var isviewmore = isViewMore
    private fun buildCardView() {
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
        val inflater: LayoutInflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_youtube, this)


        providerImageView = findViewById<View>(R.id.providerImage) as ImageView?
        imgFreeContentView = findViewById<View>(R.id.imgFreeContent) as ImageView?
        txtContentName = findViewById<View>(R.id.content_name) as TextView?
        txtContentLanguage = findViewById<View>(R.id.txtLanguage) as TextView?
        txtContentYear = findViewById<View>(R.id.txtYear) as TextView?

        mainImageView = findViewById<View>(R.id.ivPoster) as ImageView?
        ivViewMore = findViewById<View>(R.id.ivViewMore) as ImageView?
        layShadow = findViewById<View>(R.id.layShadow) as LinearLayout?
        card = findViewById(R.id.card)
        isClickable = true
        isFocusable = true

        providerImageView?.visibility = GONE
        imgFreeContentView?.visibility = GONE

        onFocusChangeListener = OnFocusChangeListener { view, b ->
            if (b) {
                layShadow?.background = resources.getDrawable(R.drawable.bg_banner_selected)
            } else {
                layShadow?.background = null
            }
        }
    }

    init {
        buildCardView()
    }
}