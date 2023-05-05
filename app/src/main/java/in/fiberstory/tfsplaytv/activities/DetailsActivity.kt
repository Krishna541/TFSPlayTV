/*
 * Copyright (c) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.appInstalledOrNot
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isMoviePlayable
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.launchAppUsingSSO
import android.graphics.drawable.Drawable
import android.widget.TextView
import android.widget.LinearLayout
import android.os.Bundle
import android.util.DisplayMetrics
import android.graphics.Bitmap
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.utility.*
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLDecoder
import java.util.*

class DetailsActivity : FragmentActivity() {
    private var movieDetail: DocumentaryItemsModel? = null
    private var btnDWatchNow: Button? = null
    private var mMainFrame: View? = null
    private var mBackgroundWithPreview: Drawable? = null
    private var mContentImage: ImageView? = null
    private var content_type: TextView? = null
    private var content_name: TextView? = null
    private var content_other: TextView? = null
    private var content_info: ExpandableTextView? = null
    private var content_cast: TextView? = null
    private var content_crew: TextView? = null
    private var content_genre: TextView? = null
    private var content_expand: TextView? = null
    private var content_details: LinearLayout? = null
    override fun onResume() {
        super.onResume()
        btnDWatchNow!!.requestFocus()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//        )
        Init()
        setData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        movieDetail = intent.getSerializableExtra("MOVIE") as DocumentaryItemsModel
        setContent()
    }

    private fun Init() {
        content_expand = findViewById<View>(R.id.expand) as TextView
        btnDWatchNow = findViewById<View>(R.id.btnDWatchNow) as Button
        mContentImage = findViewById<View>(R.id.detail_image) as ImageView
        content_type = findViewById(R.id.content_type)
        content_name = findViewById(R.id.content_name)
        content_other = findViewById(R.id.content_other)
        content_info = findViewById(R.id.content_info)
        content_cast = findViewById(R.id.content_cast)
        content_crew = findViewById(R.id.content_crew)
        content_genre = findViewById(R.id.content_genre)
        content_details = findViewById(R.id.content_details)
        val display = windowManager.defaultDisplay
        val windowSize = Point()
        display.getSize(windowSize)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val imageWidth: Int
        val imageHeight: Int
        if (width == 1280 && height == 720) {
            imageWidth = resources.getDimension(R.dimen.content_image_width_small).toInt()
            imageHeight = resources.getDimension(R.dimen.content_image_height_small).toInt()
            content_details?.layoutParams?.width = 450
        } else {
            imageWidth = resources.getDimension(R.dimen.content_image_width).toInt()
            imageHeight = resources.getDimension(R.dimen.content_image_height).toInt()
            content_details?.layoutParams?.width = 750
        }
        mContentImage!!.layoutParams.height = imageHeight
        mContentImage!!.layoutParams.width = imageWidth
        val gradientSize = resources.getDimension(R.dimen.content_image_gradient_size).toInt()
        // Create the background
        val background: Bitmap = BackgroundImageUtils.createBackgroundWithPreviewWindow(
            windowSize.x,
            windowSize.y,
            imageWidth,
            imageHeight,
            gradientSize,
            ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.black)
        )
        mBackgroundWithPreview = BitmapDrawable(resources, background)
        // Set the background
        mMainFrame = findViewById(R.id.main_frame)
        mMainFrame?.background = mBackgroundWithPreview

        btnDWatchNow!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP) {
                when(i){
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        Log.d("up", "Init: up")
                        content_expand!!.requestFocus()
                        btnDWatchNow!!.clearFocus()
                        return@OnKeyListener false
                    }
                }
            }


            false
        })

        content_expand!!.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
            if (b) {
                content_expand!!.setTextColor(ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.orange))
            } else {
                content_expand!!.setTextColor(ContextCompat.getColor(this, com.androijo.tvnavigation.R.color.white))
            }
        })

        content_expand!!.setOnKeyListener(View.OnKeyListener{ view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when(i){
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        Log.d("up", "Init: down")
                        content_expand!!.clearFocus()
                        btnDWatchNow!!.requestFocus()
                        return@OnKeyListener false
                    }
                }
            }


            false
        })

        content_expand!!.setOnClickListener {
            if (content_info!!.isExpanded) {
                content_info!!.collapse()
                content_expand!!.text = getString(R.string.content_more)
            } else {
                content_info!!.expand()
                content_expand!!.text = getString(R.string.content_less)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setContent() {
        stringDecode(content_type, "Featured On: " + movieDetail?.ottServiceName)
        if(movieDetail?.movieInfo?.length!! > 334){
            content_expand!!.visibility = View.VISIBLE
        }else{
            content_expand!!.visibility = View.GONE
        }
        movieDetail?.movieInfo?.let { stringDecode(content_info, it) }
        movieDetail?.movieName?.let { stringDecode(content_name, it) }
        if (!movieDetail?.castAndCrew.equals("", ignoreCase = true)) {
            if (movieDetail?.castAndCrew?.contains("|") == true) {
                val tokens = StringTokenizer(movieDetail?.castAndCrew, "|")
                stringDecode(content_cast, tokens.nextToken().trim { it <= ' ' })
                stringDecode(content_crew, tokens.nextToken().trim { it <= ' ' })
            } else {
                movieDetail?.castAndCrew?.trim { it <= ' ' }?.let { stringDecode(content_cast, it) }
            }
        }
        val genre1: String? = movieDetail?.genre1
        val genre2: String? = movieDetail?.genre2
        val genre3: String? = movieDetail?.genre
        val stringBuilder = StringBuilder()
        if (!genre1.equals("", ignoreCase = true)) {
            stringBuilder.append(movieDetail?.genre1)
            stringBuilder.append(",")
        }
        if (!genre2.equals("", ignoreCase = true)) {
            stringBuilder.append(movieDetail?.genre2)
            stringBuilder.append(",")
        }
        if (!genre3.equals("", ignoreCase = true)) {
            stringBuilder.append(movieDetail?.genre)
            stringBuilder.append(",")
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","))
        }
        stringDecode(content_genre, "Genre: $stringBuilder")
        val timeline: Int? = movieDetail?.duration?.toInt()
        val hours = timeline?.div(3600)
        val minutes = (timeline?.rem(3600))?.div(60)
        val time = hours.toString() + "h " + minutes + "m"
        content_other?.text =
            movieDetail?.censorRating + " | " + movieDetail?.releaseYear + " | " + time
        GlideHelper.loadImageWithCrossFadeTransition(
            this@DetailsActivity,
            mContentImage,
            movieDetail?.detailPageCoverTv,
            CONTENT_IMAGE_CROSS_FADE_DURATION,
            R.drawable.ic_action_popular_channels_bg
        )
        if (movieDetail?.detailPageCoverTv != null && movieDetail?.detailPageCoverTv?.isNotEmpty() == true) {
            mMainFrame!!.background = mBackgroundWithPreview
        } else {
            mMainFrame!!.setBackgroundColor(Color.TRANSPARENT)
        }
        btnDWatchNow!!.setOnClickListener {
            if (CheckUserLogin.loadPrefs(this) == 1) {
                if (appInstalledOrNot(movieDetail?.apkPackageId)) {
                    if (isMoviePlayable == 1) {
                        Log.d("logging", "setContent: "+ isMoviePlayable)
                        Log.d("TAG", "setContent: "+movieDetail!!.apkPackageId)
                        launchAppUsingSSO(movieDetail?.deeplinkUrlTv, movieDetail?.ottServiceName ,movieDetail?.apkPackageId)
                    } else {
                        Log.d("logging", "setContent: "+ isMoviePlayable +" "+movieDetail?.ottServiceName)
//                        noActiveSubscription(movieDetail?.ottServiceName)
                        this.noActiveSubscription1(this,movieDetail?.ottServiceName)

                    }
                } else {
//                    appNotFound(movieDetail?.ottServiceName)
                    this.appNotFound1(this,movieDetail?.ottServiceName)
                }
            } else {
                startActivity(
                    ProfileActivity.createIntentShow(
                        this
                    )
                )
            }
        }
    }

    fun appNotFound(serviceName: String?) {
        val dialog = Dialog(this@DetailsActivity, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(this@DetailsActivity.resources.getColor(R.color.colorTransparent)))
        dialog.show()
        val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
        val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
        val dialogOK = dialog.findViewById<TextView>(R.id.dialogOK)
        val dialogCancel = dialog.findViewById<TextView>(R.id.dialogCancel)
        dialogCancel.visibility = View.GONE
        dialogOK.text = "Close"
        dialogTitle.text = "App not found!"
        dialogInfo.text =
            "Please install " + serviceName + " application to view this content. Please install the application from home page."
        dialogOK.setOnClickListener {
            (this@DetailsActivity as Activity).finish()
        }
    }

    fun noActiveSubscription(serviceName: String?) {
        val dialog = Dialog(this, R.style.DialogTheme)
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(MainActivity.context.resources.getColor(R.color.colorTransparent)))
        dialog.show()
        val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
        val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
        val dialogCancel = dialog.findViewById<Button>(R.id.dialogCancel)
        dialogCancel.visibility = View.GONE
        val dialogOk = dialog.findViewById<Button>(R.id.dialogOK)
        dialogTitle.text = "No Active Subscription!"
        dialogInfo.text =
            "You don't have an active " + serviceName + " premium subscription. " + "Visit the 'Offers' section of the TFS Play mobile app to subscribe to one of our packs"
        dialogOk.text = "OK"
        dialogOk.setOnClickListener { (this@DetailsActivity as Activity).finish() }
    }


    fun stringDecode(textView: TextView?, s: String) {
        try {
            textView!!.text =
                URLDecoder.decode(s.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25"), "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        private const val CONTENT_IMAGE_CROSS_FADE_DURATION = 1500
        fun createIntentMovie(context: Context?, movieDetail: DocumentaryItemsModel): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("MOVIE", movieDetail)
            return intent
        }
    }
}