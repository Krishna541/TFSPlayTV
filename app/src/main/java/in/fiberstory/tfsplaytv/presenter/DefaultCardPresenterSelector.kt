/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package `in`.fiberstory.tfsplaytv.presenter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.fragments.BannerCardPresenter
import `in`.fiberstory.tfsplaytv.fragments.HomePageCardPresenter
import `in`.fiberstory.tfsplaytv.fragments.HomePageYoutubePresenter
import android.content.Context
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import java.util.HashMap

/**
 * This PresenterSelector will decide what Presenter to use depending on a given card's type.
 */
class DefaultCardPresenterSelector(private val mContext: Context, private val Type: String) :
    PresenterSelector() {
    private val presenters: HashMap<String, Presenter?> = HashMap<String, Presenter?>()
    override fun getPresenter(item: Any): Presenter? {
        var presenter: Presenter? = presenters[Type]
        if (presenter == null) {
            when (Type) {
                "Banner" -> presenter = BannerCardPresenter(mContext, R.style.GridCardStyle)
                "Youtube" -> presenter = HomePageYoutubePresenter(mContext, R.style.GridCardStyle)
                "Home" -> presenter = HomePageCardPresenter(mContext, R.style.GridCardStyle)
                "LiveTV" -> presenter = LiveTVCardPresenter(mContext, R.style.GridCardStyle)
                "Setting" -> presenter = SettingCardPresenter(mContext,R.style.GridCardStyle)
            }
        }
        presenters[Type] = presenter
        return presenter
    }
}