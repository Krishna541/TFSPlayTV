package `in`.fiberstory.tfsplaytv.model

import `in`.fiberstory.tfsplaytv.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.leanback.widget.BaseCardView

open class NewDocumenteryImageCardView(context: Context?, private val type: String) :
    BaseCardView(context) {
    var mainImageView: ImageView? = null
    var ivLoadMore: ImageView? = null
    var channelImageview: ImageView? = null
    var providerImageView: ImageView? = null
    var imgFreeContentView: ImageView? = null
    var txtContentName: TextView? = null
    var txtContentLanguage: TextView? = null
    var txtContentYear: TextView? = null
    var card: CardView? = null
    var layShadow: LinearLayout? = null
    var layParent: LinearLayout? = null
    var isviewmore = false
    private fun buildCardView() {
        // Make sure this view is clickable and focusable
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
        val inflater: LayoutInflater = LayoutInflater.from(context)
        if (type == "Short Films") {
            inflater.inflate(R.layout.item_short_film, this)
        } else {
            inflater.inflate(R.layout.item_documentaries, this)
        }
        ivLoadMore = findViewById<View>(R.id.ivLoadMore) as ImageView?
        mainImageView = findViewById<View>(R.id.ivPoster) as ImageView?
        providerImageView = findViewById<View>(R.id.providerImage) as ImageView?
        imgFreeContentView = findViewById<View>(R.id.imgFreeContent) as ImageView?
        txtContentName = findViewById<View>(R.id.content_name) as TextView?
        txtContentLanguage = findViewById<View>(R.id.txtLanguage) as TextView?
        txtContentYear = findViewById<View>(R.id.txtYear) as TextView?
        layShadow = findViewById<View>(R.id.layShadow) as LinearLayout?
        card = findViewById(R.id.card)
        //      movie_gradient = findViewById(R.id.movie_gradient);
        isClickable = true
        isFocusable = true
        onFocusChangeListener = OnFocusChangeListener { view, b ->
            Log.e("focus", "changed")
            if (b) {
                layShadow!!.background = resources.getDrawable(R.drawable.bg_banner_selected)
            } else {
                layShadow!!.background = null
            }
        }


    }

    fun setMainImage(drawable: Drawable?) {
        mainImageView!!.setImageDrawable(drawable)
    }

    fun ActivateChannel() {
        channelImageview!!.visibility = View.VISIBLE
        //        movie_gradient.setVisibility(GONE);
        mainImageView!!.visibility = View.GONE
    }

    fun DeactivateChannle() {
        channelImageview!!.visibility = View.GONE
        //      movie_gradient.setVisibility(VISIBLE);
        mainImageView!!.visibility = View.VISIBLE
    }

    fun setMainImageDimensions(width: Int, height: Int) {
        val lp: ViewGroup.LayoutParams = mainImageView!!.layoutParams
        lp.width = width
        lp.height = height
        mainImageView!!.layoutParams = lp
    }

    // View movie_gradient;
    init {
        buildCardView()
    }
}