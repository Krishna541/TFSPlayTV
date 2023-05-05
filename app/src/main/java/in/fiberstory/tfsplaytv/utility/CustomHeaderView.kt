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
 */
package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.ProfileActivity
import `in`.fiberstory.tfsplaytv.activities.SearchActivity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.TitleViewAdapter

open class CustomHeaderView @RequiresApi(api = Build.VERSION_CODES.O) constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
) : RelativeLayout(context, attrs, defStyle), TitleViewAdapter.Provider {
    var mTitleView: TextView
    var mSearch: ImageView
    var mProfile: ImageView
    var mHorizontalGridView: HorizontalGridView
    lateinit var mClockView: TextClock
    var mTopHeader: RelativeLayout
    val mTitleViewAdapter: TitleViewAdapter = object : TitleViewAdapter() {

        override fun setTitle(titleText: CharSequence) {
            this@CustomHeaderView.setTitle(titleText)
        }

        override fun setBadgeDrawable(drawable: Drawable) {}

        override fun getSearchAffordanceView(): View? {
          return null
        }


        override fun setOnSearchClickedListener(listener: OnClickListener) {}
        override fun updateComponentsVisibility(flags: Int) {}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    constructor(context: Context) : this(context, null) {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    init {
        val root: View = LayoutInflater.from(context).inflate(R.layout.custom_header_more, this)
        mTitleView = root.findViewById(R.id.title_tv)
        mHorizontalGridView = root.findViewById<HorizontalGridView>(R.id.horizontal_grid)
//        mClockView = root.findViewById(R.id.clock)
        mTopHeader = root.findViewById(R.id.topHeader)
        mProfile = root.findViewById(R.id.image_profile)
        mSearch = root.findViewById(R.id.image_search)
        mSearch.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    SearchActivity::class.java
                )
            )
        }
        mProfile.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProfileActivity::class.java
                )
            )
        }

        if (screenWidth == 1280 && screenHeight == 720) {
            mTopHeader.layoutParams.height = 64
        } else {
            mTopHeader.layoutParams.height = 95
        }
    }

    fun setTitle(title: CharSequence?) {
        if (title != null) {
            mTitleView.text = title
        }
    }

    override fun getTitleViewAdapter(): TitleViewAdapter {
        return mTitleViewAdapter
    }

    companion object {
        val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }
}