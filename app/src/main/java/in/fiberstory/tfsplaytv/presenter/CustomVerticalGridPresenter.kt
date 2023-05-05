package `in`.fiberstory.tfsplaytv.presenter

import androidx.leanback.widget.VerticalGridPresenter

class CustomVerticalGridPresenter(zoom: Int, `val`: Boolean) : VerticalGridPresenter(zoom, `val`) {
    override fun initializeGridViewHolder(vh: ViewHolder) {
        super.initializeGridViewHolder(vh)
        vh.gridView.verticalSpacing = 16
        vh.gridView.horizontalSpacing = 16
        vh.gridView.isVerticalScrollBarEnabled = true
        val top = 40 //this is the new value for top padding
        val bottom = vh.gridView.paddingBottom
        val right = vh.gridView.paddingRight
        val left = 30
        vh.gridView.setPadding(left, top, right, bottom)
    }

    // Cardview Shadow ...
    override fun isUsingDefaultShadow(): Boolean {
        return true
    }
}