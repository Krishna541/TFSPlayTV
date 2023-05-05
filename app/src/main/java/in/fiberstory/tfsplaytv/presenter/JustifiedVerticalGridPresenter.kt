package `in`.fiberstory.tfsplaytv.presenter

import android.content.Context
import androidx.leanback.widget.VerticalGridPresenter
import androidx.leanback.widget.VerticalGridView

class JustifiedVerticalGridPresenter(private val context : Context?,focusZoomFactor: Int, dimmingEffect: Boolean) :
    VerticalGridPresenter(focusZoomFactor, dimmingEffect) {
    /**
     * @return The [VerticalGridView] used by this presenter.
     */

    private lateinit var gridView: VerticalGridView

    override fun initializeGridViewHolder(vh: ViewHolder) {
        super.initializeGridViewHolder(vh)
        gridView = vh.gridView
        vh.gridView.verticalSpacing = 16
        vh.gridView.horizontalSpacing = 16
        gridView.isVerticalScrollBarEnabled = true
        val top = 40 //this is the new value for top padding
        val bottom = gridView.paddingBottom
        val right = gridView.paddingRight
        val left = 50
        gridView.setPadding(left, top, right, bottom)
    }

    override fun isUsingDefaultShadow(): Boolean {
        return true
    }
}