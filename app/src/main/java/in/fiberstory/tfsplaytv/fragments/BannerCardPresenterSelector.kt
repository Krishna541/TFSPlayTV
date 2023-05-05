package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import android.content.Context
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

class BannerCardPresenterSelector(
    private val mContext: Context,
    private val Type: String
) :
    PresenterSelector() {

    override fun getPresenter(item: Any?): Presenter {
        return if (Type == "Banner") {
            BannerCardPresenter(mContext, R.style.GridCardStyle)
        } else {
            HomePageCardPresenter(mContext, R.style.GridCardStyle)
        }

    }

    override fun getPresenters(): Array<Presenter> {
        return super.getPresenters()
    }

}







