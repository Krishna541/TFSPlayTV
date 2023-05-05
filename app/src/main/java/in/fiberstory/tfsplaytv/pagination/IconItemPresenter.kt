package `in`.fiberstory.tfsplaytv.pagination

import androidx.leanback.widget.Presenter
import android.view.ViewGroup
import androidx.core.content.ContextCompat

class IconItemPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val iconCardView = IconCardView(parent.context)
        return ViewHolder(iconCardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (item is Option) {
            val option = item
            val optionView = viewHolder.view as IconCardView
            optionView.setMainImageDimensions(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            optionView.setOptionTitleText(option.title)
            val value = option.value
            if (value != null) optionView.setOptionValueText(option.value)
            val context = viewHolder.view.context
            optionView.setOptionIcon(ContextCompat.getDrawable(context, option.iconResource))
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}

    companion object {
        private const val GRID_ITEM_WIDTH = 320
        private const val GRID_ITEM_HEIGHT = 180
    }
}