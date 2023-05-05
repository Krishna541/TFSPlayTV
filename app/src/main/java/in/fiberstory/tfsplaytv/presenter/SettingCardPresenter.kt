package `in`.fiberstory.tfsplaytv.presenter


import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.fragments.AbstractCardPresenter
import `in`.fiberstory.tfsplaytv.model.SettingModel
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.BaseCardView

/**
 * This Presenter will display a card consisting of an image on the left side of the card followed
 * by text on the right side. The image and text have equal width. The text will work like a info
 * box, thus it will be hidden if the parent row is inactive. This behavior is unique to this card
 * and requires a special focus handler.
 */
class SettingCardPresenter(context: Context, themeResID: Int) :
    AbstractCardPresenter<BaseCardView>(context, themeResID) {

    override fun onCreateView(): BaseCardView {
        val cardView = BaseCardView(context, null, androidx.leanback.R.style.Widget_Leanback_BaseCardViewStyle)
        cardView.isFocusable = true
        cardView.addView(LayoutInflater.from(context).inflate(R.layout.item_setting, null, false))
        return cardView
    }

    override fun onBindViewHolder(card: Any, cardView: BaseCardView) {
        val data = card as SettingModel

        val textView = cardView.findViewById<TextView>(R.id.txt_Name)
        val img_Icon = cardView.findViewById<ImageView>(R.id.img_icon)

        textView.text = data.Name
        data.Icon?.let { img_Icon.setImageResource(it) }
    }


}
