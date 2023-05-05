package `in`.fiberstory.tfsplaytv.fragments

import android.content.Context


import android.view.LayoutInflater
import android.widget.TextView
import androidx.leanback.widget.BaseCardView
import `in`.fiberstory.tfsplaytv.R

/**
 * This Presenter will display a card consisting of an image on the left side of the card followed
 * by text on the right side. The image and text have equal width. The text will work like a info
 * box, thus it will be hidden if the parent row is inactive. This behavior is unique to this card
 * and requires a special focus handler.
 */
class CardPresenter(context: Context) :
    AbstractCardPresenter<BaseCardView>(context) {

    override fun onCreateView(): BaseCardView {
        val cardView = BaseCardView(context, null, androidx.leanback.R.style.Widget_Leanback_BaseCardViewStyle)
        cardView.isFocusable = true
        cardView.addView(LayoutInflater.from(context).inflate(R.layout.card_subject, null, false))
        return cardView
    }

    override fun onBindViewHolder(card: Any, cardView: BaseCardView) {
        val data = card as String
        val textView = cardView.findViewById<TextView>(R.id.cardName)
        textView.text = data

    }



}
