package `in`.fiberstory.tfsplaytv.pagination

import `in`.fiberstory.tfsplaytv.R
import androidx.leanback.widget.BaseCardView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlin.jvm.JvmOverloads
import android.content.Context
import android.view.LayoutInflater
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageView

class IconCardView : BaseCardView {
    var mLayout: RelativeLayout? = null
    var mIcon: ImageView? = null
    var mTitle: TextView? = null
    var mValue: TextView? = null

    constructor(context: Context?, styleResId: Int) : super(
        ContextThemeWrapper(
            context,
            styleResId
        ), null, 0
    ) {
        buildptionCardView(styleResId)
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = androidx.leanback.R.attr.imageCardViewStyle
    ) : super(
        getStyledContext(context, attrs, defStyleAttr), attrs, defStyleAttr
    ) {
        buildptionCardView(getImageCardViewStyle(context, attrs, defStyleAttr))
    }

    private fun buildptionCardView(styleResId: Int) {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true
        //setCardType(CARD_TYPE_INFO_UNDER);
        cardType = CARD_TYPE_MAIN_ONLY
        setBackgroundResource(R.color.colorAccent)
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.view_options_item, this)
        mLayout = view.findViewById(R.id.layout_option_card)
        mIcon = view.findViewById(R.id.image_option)
        mTitle = view.findViewById(R.id.text_option_title)
        mValue = view.findViewById(R.id.text_option_value)
        val cardAttrs = context.obtainStyledAttributes(
            styleResId, androidx.leanback.R.styleable.lbImageCardView
        )
        cardAttrs.recycle()
    }

    fun setMainImageDimensions(width: Int, height: Int) {
        val lp = mLayout!!.layoutParams
        lp.width = width
        lp.height = height
        mLayout!!.layoutParams = lp
    }

    fun setOptionIcon(drawable: Drawable?) {
        mIcon!!.setImageDrawable(drawable)
    }

    fun setOptionTitleText(titleText: String?) {
        mTitle!!.text = titleText
    }

    fun setOptionValueText(valueText: String?) {
        mValue!!.text = valueText
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    companion object {
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
            // Read style attribute defined in XML layout.
            var style = attrs?.styleAttribute ?: 0
            if (0 == style) {
                // Not found? Read global ImageCardView style from Theme attribute.
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