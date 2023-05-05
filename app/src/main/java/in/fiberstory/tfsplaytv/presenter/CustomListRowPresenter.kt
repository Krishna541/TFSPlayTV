package `in`.fiberstory.tfsplaytv.presenter

import android.app.Activity
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.RowPresenter

class CustomListRowPresenter(activity: Activity?) : ListRowPresenter() {
    private var vh: ViewHolder? = null
    override fun setEntranceTransitionState(
        holder: RowPresenter.ViewHolder,
        afterEntrance: Boolean
    ) {
        super.setEntranceTransitionState(holder, afterEntrance)
//        (holder as ViewHolder).gridView.setChildrenVisibility(
//            if (afterEntrance) View.VISIBLE else View.INVISIBLE
//        )
    }

    override fun onBindRowViewHolder(holder: RowPresenter.ViewHolder, item: Any) {
        super.onBindRowViewHolder(holder, item)
        vh = holder as ViewHolder
        val top =  vh!!.gridView.paddingTop //this is the new value for top padding
        val bottom = vh!!.gridView.paddingBottom
        val right = vh!!.gridView.paddingRight
        val left = 50
        vh!!.gridView.setPadding(left, top, right, bottom)
        vh!!.gridView.setSelectedPositionSmooth(0)
        vh!!.gridView.setItemSpacing(16)
//        vh!!.gridView.fadingLeftEdge = true
//        vh!!.gridView.fadingLeftEdgeOffset = 0
//        vh!!.gridView.fadingLeftEdgeLength = 100
    }

    // Other row default shadow gone ...
    override fun isUsingDefaultListSelectEffect(): Boolean {
        return true
    }

    // Cardview Shadow ...
    override fun isUsingDefaultShadow(): Boolean {
        return true
    }

    init {
        headerPresenter = CustomRowHeaderPresenter(activity)
    }
}