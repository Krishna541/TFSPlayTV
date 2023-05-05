package `in`.fiberstory.tfsplaytv.fragments

import android.content.Context
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

class CardPresenterSelector(private val mContext: Context) : PresenterSelector() {

    override fun getPresenter(item: Any?): Presenter? {
        return CardPresenter(mContext)
    }

    override fun getPresenters(): Array<Presenter> {
        return super.getPresenters()
    }

}







