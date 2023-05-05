package `in`.fiberstory.tfsplaytv.pagination

import `in`.fiberstory.tfsplaytv.R
import androidx.leanback.widget.BaseCardView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlin.jvm.JvmOverloads
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View

class LoadingPortraitCardView : BaseCardView {
    private var mProgressBar: ProgressBar? = null
    private var mProgressContainer: RelativeLayout? = null

    constructor(context: Context?, styleResId: Int) : super(
        ContextThemeWrapper(
            context,
            styleResId
        ), null, 0
    ) {
        buildLoadingCardView(styleResId)
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = androidx.leanback.R.attr.imageCardViewStyle
    ) : super(
        getStyledContext(context, attrs, defStyleAttr), attrs, defStyleAttr
    ) {
        buildLoadingCardView(getImageCardViewStyle(context, attrs, defStyleAttr))
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    private fun buildLoadingCardView(styleResId: Int) {
        isFocusable = false
        isFocusableInTouchMode = false
        cardType = CARD_TYPE_MAIN_ONLY
        setBackgroundResource(R.color.colorTransparent)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_loading_card, this)
        val cardAttrs = context.obtainStyledAttributes(
            styleResId, androidx.leanback.R.styleable.lbImageCardView
        )
        mProgressBar = findViewById<View>(R.id.progress_indicator) as ProgressBar
        mProgressContainer = findViewById<View>(R.id.progressContainer) as RelativeLayout
        if (screenWidth == 1280 && screenHeight == 720) {
            mProgressContainer!!.layoutParams.width = 130
            mProgressContainer!!.layoutParams.height = 183
        } else {
            mProgressContainer!!.layoutParams.width = 195
            mProgressContainer!!.layoutParams.height = 275
        }
        cardAttrs.recycle()
    }

    fun isLoading(isLoading: Boolean) {
        mProgressBar!!.visibility = if (isLoading) VISIBLE else GONE
    }

    companion object {
        val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels

        private fun getStyledContext(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ): Context {
            val style = getImageCardViewStyle(context, attrs, defStyleAttr)
            return ContextThemeWrapper(context, style)
        }

        private fun getImageCardViewStyle(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ): Int {
            var style = attrs?.styleAttribute ?: 0
            if (0 == style) {
                val styledAttrs = context.obtainStyledAttributes(
                    androidx.leanback.R.styleable.LeanbackTheme
                )
                style = styledAttrs.getResourceId(
                    androidx.leanback.R.styleable.LeanbackTheme_imageCardViewStyle, 0
                )
                styledAttrs.recycle()
            }
            return style
        }
    }
}