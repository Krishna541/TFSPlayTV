package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.adapter.OttModelAdapter
import `in`.fiberstory.tfsplaytv.fragments.EpisodeFragment
import `in`.fiberstory.tfsplaytv.fragments.PlexigoEpisodeFragment
import `in`.fiberstory.tfsplaytv.model.*
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.network.PlexigoAPIClient
import `in`.fiberstory.tfsplaytv.utility.*
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.plexigo_content_detail_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class PlexigoOnContentDetailActivity : FragmentActivity(),OttModelAdapter.OnOttClickListener,
    PlexigoEpisodeFragment.OnBrowseRowListener {

    private var movieDetail: MovieDetailModel? = null
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
    lateinit var sharedPreferences: SharedPreferences
    var enableOttFocus = false
    var rv_ott:RecyclerView? = null
    lateinit var context : Context
    var apiCall: Call<String>? = null
    var userId:kotlin.Int = 0
    private var apiInterface: APIInterface? = null
    private var content_details: LinearLayout? = null
    val REQUEST_CODE = 132
    var REQUEST_CODE_PAYMENT = 1001
    var movieID : String?=null
    var last_selected = "info"
    var isLive = false
    var isFestivalMovie = false

    override fun onResume() {
        super.onResume()
        rv_ott!!.requestFocus()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plexigo_content_detail_activity)
        context = this@PlexigoOnContentDetailActivity
        movieID = intent.getStringExtra("movieId")
        userId = intent.getIntExtra("userId", 0)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode) {
            if (resultCode == 1) { //success
                Toast.makeText(context, "SUCCESS!", Toast.LENGTH_LONG).show()
                PlayVideo()
            } else if (resultCode == 2) { //failure
                Toast.makeText(context, "FAILED", Toast.LENGTH_LONG).show()
            } else if (resultCode == 3) { // aborted
                Toast.makeText(context, "ABORTED", Toast.LENGTH_LONG).show()
            } else if (resultCode == 4) { // Timeout - no response in 5 min
                Toast.makeText(context, "TIMEOUT", Toast.LENGTH_LONG).show()
            }
        } else if (REQUEST_CODE_PAYMENT == requestCode) {
            if (resultCode == 200) { //success
                Toast.makeText(context, "Payment Success!", Toast.LENGTH_SHORT).show()
                PlayVideo()
                //                getVideoDetails();
            } else if (resultCode == 400) { //failure
                Toast.makeText(
                    context,
                    "Transaction failed! Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (resultCode == 413) { // aborted
                Toast.makeText(context, "Transaction cancelled !", Toast.LENGTH_SHORT).show()
            } else if (resultCode == 11) { //back pressed from payment page by user
                Toast.makeText(context, "Transaction cancelled !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun PlayVideo() {
        //val videoUrl: String = movieDetail?.videopath!!
        if (!TextUtils.isEmpty(movieDetail!!.isDRMContent) && movieDetail!!.isDRMContent
                .equals("y",ignoreCase = true)
        ) { // DRM content
            GetContentToken()
        } else {
            Toast.makeText(context, "No Video details found", Toast.LENGTH_SHORT).show()
        }
    }

    fun PlayInOtherOtt(url: String?, openInBrowser: Boolean) {
        val location = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, location)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "This app is not available on your mobile",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkUserExistAPI() {
        userId = sharedPreferences.getInt("PlexigoUserId", 0)
        Log.d("TAG", "checkUserExistAPI: "+userId)
        getMovieDetailAPI(userId)
    }

    private fun getMovieDetailAPI(plexigoUserID: Int) {
        apiInterface = PlexigoAPIClient.getClient("1.3", APIInterface.DOMAIN_URL)!!
            .create(APIInterface::class.java)
        val apiCall : Call<String>? = apiInterface?.getMovieDetailAPI(Integer.valueOf(movieID), plexigoUserID)

        apiCall!!.enqueue(object : Callback<String?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieDetailResponseDataModel: MovieDetailResponseModel =
                                gson.fromJson(
                                    reader,
                                    MovieDetailResponseModel::class.java
                                )
                            if (movieDetailResponseDataModel.statusCode != null && movieDetailResponseDataModel.statusCode
                                    .equals("200")
                            ) {
                                val model: MovieDetailResponseDataModel =
                                    movieDetailResponseDataModel.data!!
                                val all: ArrayList<MovieDetailModel> = model.contentdetail!!
                                if (model.contentdetail!!.isNotEmpty()) {
                                    movieDetail = model.contentdetail[0]
                                    if(model.contentdetail[0].seasons != null && model.contentdetail[0].seasons!!.size > 0){
                                        val bundle = Bundle()
                                        val data = ArrayList<TvSeriesEpisode>()
                                        for(i in 0 until  model.contentdetail[0].seasons!!.size){
                                            data!!.addAll(model.contentdetail[0].seasons!![i].episodes!!)
                                        }
                                        bundle.putSerializable("episodeList",data )
                                        val mBrowseFragment = PlexigoEpisodeFragment()
                                        mBrowseFragment.arguments = bundle
                                        supportFragmentManager.beginTransaction().add(R.id.episodePanel, mBrowseFragment)
                                            .commit()
                                    }
                                    setContent()
                                }

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "" + response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: java.lang.IllegalStateException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(context, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun GetContentToken() {
        apiInterface =
            PlexigoAPIClient.getClient("", APIInterface.DOMAIN_URL)?.create(APIInterface::class.java)
        apiCall = apiInterface?.getContentTokenAPI(movieID, userId)
        apiCall?.enqueue(object : Callback<String?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body())
                            val contentTokenResModel: ContentTokenResModel = gson.fromJson(
                                reader,
                                ContentTokenResModel::class.java
                            )
                            if (contentTokenResModel.status == "200") {
                                val token: String = contentTokenResModel.contentToken!!
                                var SelectedWatchDuration = 0

                                SelectedWatchDuration = movieDetail?.watchDuration!!.toInt()

                                intent.putExtra("PlexigoUserID", userId)
                                intent.putExtra("WatchDuration", SelectedWatchDuration)
                                intent.putExtra("token", token)
                                var contentId: String? = ""
                                startActivity(
                                    ExoplayerActivity.createIntentExoplayer(this@PlexigoOnContentDetailActivity
                                    ,contentTokenResModel.dashURL!!,contentTokenResModel.widevineLicenseURL!!,token,SelectedWatchDuration,movieID!!.toInt())
                                )
                            } else {
                                if (contentTokenResModel.message != null) {
                                    Toast.makeText(
                                        context,
                                        "" + contentTokenResModel.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "" + response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(context, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun CheckIfUserPpvSubscribed() {

        val req = PlexigoChannelCheckExpReqModel(
            Integer.valueOf(userId), Integer.valueOf(movieID), movieDetail!!.ppvValidity!!.toInt(),
            if (movieDetail?.ppvType.equals("hours")) "Hour" else movieDetail?.ppvType!!
        )
        apiInterface =
            PlexigoAPIClient.getClient("1.0", APIInterface.CONTENT_EXPIRY_BASE_URL)!!.create(
                APIInterface::class.java
            )
        apiCall = apiInterface?.checkExpiry(req)
        apiCall!!.enqueue(object : Callback<String?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body())
                            val contentExpiryResModel: ContentExpiryResModel = gson.fromJson(
                                reader,
                                ContentExpiryResModel::class.java
                            )
                            if (contentExpiryResModel.validationStatus
                                    .equals("Valid")
                            ) { //already paid
                                PlayVideo()
                            } else if (contentExpiryResModel.validationStatus
                                    .equals("Expired")
                            ) { //Plan Expired
                                //Plan Expired
                                Toast.makeText(
                                    context,
                                    "PPV Plan Expired! Please make payment from mobile app",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else if (contentExpiryResModel.validationStatus
                                    .equals("Not Purchased")
                            ) { //Plan Expired
                                //Plan Expired
                                Log.d("TAG", "onResponse: 2 time")
                                plexigoMovie(context)

                               // Toast.makeText(context, R.string.ppv_payment, Toast.LENGTH_LONG).show();


//                                Toast.makeText(
//                                    context,
//                                    "Please make payment from mobile app.",
//                                    Toast.LENGTH_SHORT
//                                ).show()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Validation Status " + contentExpiryResModel.validationStatus,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "" + response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: java.lang.IllegalStateException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        context,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(context, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        checkUserExistAPI()
        //setContent()
    }

    private fun Init() {
        content_expand = findViewById<View>(R.id.expand) as TextView
        mContentImage = findViewById<View>(R.id.detail_image) as ImageView
        content_type = findViewById(R.id.content_type)
        content_name = findViewById(R.id.content_name)
        content_other = findViewById(R.id.content_other)
        content_info = findViewById(R.id.content_info)
        content_cast = findViewById(R.id.content_cast)
        content_crew = findViewById(R.id.content_crew)
        content_genre = findViewById(R.id.content_genre)
        content_details = findViewById(R.id.content_details)
        rv_ott = findViewById(R.id.rv_ott)
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

        rv_ott!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP) {
                when(i){
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        content_expand!!.requestFocus()
                        rv_ott!!.clearFocus()
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
                        content_expand!!.clearFocus()
                        rv_ott!!.requestFocus()
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


        val manager_ott: LinearLayoutManager =
            object : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
                override fun onInterceptFocusSearch(focused: View, direction: Int): View? {
                    if (direction == View.FOCUS_RIGHT) {
                        val pos = getPosition(focused)
                        if (pos == movieDetail!!.all_ott_models!!.size -1
                        ) return focused
                    }
                    if (direction == View.FOCUS_LEFT) {
                        val pos = getPosition(focused)
                        if (pos == 0) return focused
                    }
                    /*if(direction == View.FOCUS_UP){
                    focused.setNextFocusUpId(R.id.lay_watchlist);
                    return  lay_watchlist;
                }*///if (direction == View.FOCUS_DOWN) {
//                        if (last_selected == "info") {
//                            focused.nextFocusDownId = R.id.txt_info
//                            return txt_info
//                        } else if (last_selected == "cast") {
//                            focused.nextFocusDownId = R.id.txt_cast
//                            return txt_cast
//                        }
//                    }
                    return super.onInterceptFocusSearch(focused, direction)
                }
            }
        rv_ott!!.setLayoutManager(manager_ott)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setContent() {
//        stringDecode(content_type, "Featured On: " + movieDetail?.all_ott_models)
        if(movieDetail?.synopsis?.length!! > 334){
            content_expand!!.visibility = View.VISIBLE
        }else{
            content_expand!!.visibility = View.GONE
        }
        movieDetail?.synopsis?.let { stringDecode(content_info, it) }
        movieDetail?.contentname?.let { stringDecode(content_name, it) }
        val stringBuilderAllCast = StringBuilder()
        for (i in movieDetail?.all_casts!!){
            stringBuilderAllCast.append(i.name)
            stringBuilderAllCast.append(",")
        }
        if (stringBuilderAllCast.isNotEmpty()) {
            stringBuilderAllCast.deleteCharAt(stringBuilderAllCast.lastIndexOf(","))
        }
        stringDecode(content_cast, "Cast: $stringBuilderAllCast")
//        if (!movieDetail?.all_casts!!.equals("", ignoreCase = true)) {
//            if (movieDetail?.castAndCrew?.contains("|") == true) {
//                val tokens = StringTokenizer(movieDetail?.castAndCrew, "|")
//                stringDecode(content_cast, tokens.nextToken().trim { it <= ' ' })
//                stringDecode(content_crew, tokens.nextToken().trim { it <= ' ' })
//            } else {
//                movieDetail?.castAndCrew?.trim { it <= ' ' }?.let { stringDecode(content_cast, it) }
//            }
//        }
        val genre1: String? = movieDetail?.genere
//        val genre2: String? = movieDetail?.genre1
//        val genre3: String? = movieDetail?.genre2mov
        val stringBuilder = StringBuilder()
        if (!genre1.equals("", ignoreCase = true)) {
            stringBuilder.append(movieDetail?.genere)
            stringBuilder.append(",")
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","))
        }
        stringDecode(content_genre, "Genre: $stringBuilder")
        val time =  movieDetail?.duration
        content_other?.text =
            movieDetail?.censorrating + " | " + movieDetail?.releaseyear + " | " + time
        GlideHelper.loadImageWithCrossFadeTransition(
            this,
            mContentImage,
            movieDetail?.imagepath,
            CONTENT_IMAGE_CROSS_FADE_DURATION,
            R.drawable.ic_action_popular_channels_bg
        )
        if (movieDetail?.imagepath != null && movieDetail?.imagepath?.isNotEmpty() == true) {
            mMainFrame!!.background = mBackgroundWithPreview
        } else {
            mMainFrame!!.setBackgroundColor(Color.TRANSPARENT)
        }
        rv_ott!!.setOnClickListener {
            startActivity(
                ExoplayerActivity.createIntentExoplayer(
                    this,"https://dctnnmi9jrvf6.cloudfront.net/wmt:eyJhbGciOiJSUzI1NiIsImtpZCI6ImtleV8xIiwidHlwIjoiSldUIn0.eyJpYXQiOjE2NjcyODMzOTgsImlzcyI6IlBsZXhpZ28iLCJ3bXBhdCI6IkJEZVZwVWVJWXdjMU5iMlNHWkpFS3V6TS9Eejd0MTQ5T0NUcGw2VFBWWE1DRkhBRy9lSHQySlN0dXBhSThwMFgwWGVwczlTdSt1MzVEeDZPcWxiN3hGVzZWWnM2MzdJVE1qZnZGU2g4dzh2L0dVTXNwaVQzWllZSnpOY0VOcklUTzVBSFV4TU1JNVgraE0vLzZJRGxVMzQ1TjNzaVFBb1l0OGZ6bWNNNTN5TnpYS1orUzNsamRRei91S1dRVzhXZ3hUa0JhZzNETTdGWmw4YWEvMGF0RWV1cVVpZWYvTm0wTUVTaUNiZWlOcERwTUFza3JPQzhVdjhXaVN2Z1BreTYweC8ydlZNQ2E3NCt3VzVUdmF2OVBvVGYwaC8zaUdYTkw5L3hBbzNQemkxY3pnVzcyQlc0d2dITngxVmpUTDAyRFI1WEthT0lNOTRCQXBaT1pmMHRHZz09Iiwid21wYXRmbXQiOiJiYXNlNjQiLCJ3bXBhdGxlbiI6MjA0OCwid212ZXIiOjF9.JZq1CXh9QxMri-Lb8VvtLeoujNdpRyRyra16ThJ-vUEUdY6nytbPLv7EHpTHYkKpuBv6sHwkb9U14SqHO2j48MeuckKlXOudYI0JZZCgnoLUjj8NXiU_ekoCU0lJA9Mx6hKEXAml47pRfnylHFrGQA1YSPmF8VWex_a-IsZ5QsBqQlPVfCWxgVq_iFl8E5tmxcgBD7mDn2_M-Z_FV8IF14pqZnOkf3c9SOMXmCPsum_kHxaYUr3NsPMQ2SLc_WkmqDgsZSkK4KzU6H9rm4MskhJ_mno3RuG1n6fDCNdGmn3al-r14eAEJO2Ch5Lptn-NsmIQH20h3aDaMHIDRsvFww/Content/Movies/MV_TigerPoint_Hindi/Movie/MV_AdventureKids_Cypher_Hindi_2ch_23775wmk_final_a.mpd?Expires=1667562398&Signature=kkXx~aDmeFwqBzcS2aKGYfdkDBf9oyZCfJM~YZLHZ6Zysixq5KR4WI7o1o4mFEw4goIWHkNx7tBM0PiZsqVOpCRVmyT7xB97FTvNa~1jUEKjxExsOHC-3K0wEbA6FEQdnd24wraRj9kJqSIXXxnriNYryNxeyWLozJhnnn20pQb4Q8ZoGUmYVbARBv5P~2F2C9F~LtpqlnW9Ns3XgReJkVGWYKmzuHoRe1MADpJlsfKMtOz7MxIpm9cnqAk48uBg1jkvcsRwGwJDCmek6JXnxEMwYCQ-X2CRV9LXA7QLCASF9ewQjsJp5h1wWDD6xjpoxOEfN52-PT61CgpKEoyjfw__&Key-Pair-Id=APKAJ33H2SGDXVDF2HLQ"
                    ,"https://ufomru6p.anycast.nagra.com/UFOMRU6P/wvls/contentlicenseservice/v1/licenses","eyJraWQiOiI1OTg2MTEiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZXIiOiIxLjAiLCJ0eXAiOiJDb250ZW50QXV0aFoiLCJjb250ZW50UmlnaHRzIjpbeyJjb250ZW50SWQiOiIyMzc3NSIsInVzYWdlUnVsZXNQcm9maWxlSWQiOiJUZXN0In1dfQ.YMLWKiQCEWTQ8mqWGRTNaHRKmjzGeVnOMYgj-1oEaL0"
                    ,1931,23775
                )
            )
//            if (CheckUserLogin.loadPrefs(this) == 1) {
//                if (appInstalledOrNot(movieDetail?.apkPackageId)) {
//                    if (isMoviePlayable == 1) {
//                        Log.d("logging", "setContent: "+ isMoviePlayable)
//                        launchAppUsingSSO(movieDetail?.deeplinkUrlTv, movieDetail?.ottServiceName)
//                    } else {
//                        Log.d("logging", "setContent: "+ isMoviePlayable +" "+movieDetail?.ottServiceName)
//                        noActiveSubscription(movieDetail?.ottServiceName)
//                    }
//                } else {
//                    appNotFound(movieDetail?.ottServiceName)
//                }
//            } else {
//                startActivity(
//                    ProfileActivity.createIntentShow(
//                        this
//                    )
//                )
//            }
        }


        /*ArrayList<OTTModel> otts_filtered = new ArrayList<>();
        for(int i=0;i<movie.getAll_ott_models().size();i++){
            if(movie.getAll_ott_models().get(i).getOttPlatform().equalsIgnoreCase("plexigo")){
                otts_filtered.add(movie.getAll_ott_models().get(i));
            }

        }*/
        Log.e("movie", "setContent: "+movieDetail!!.all_ott_models!!.size )
        for (i in 0 until movieDetail!!.all_ott_models!!.size) {
            if (movieDetail!!.all_ott_models!!.get(i).imagePath
                    .equals("https://plexigostatic.s3.ap-south-1.amazonaws.com/OTTImages/TheatersNearYou.png") || movieDetail!!.all_ott_models!!
                    .get(i).imagePath
                    .equals("https://plexigostatic1.s3.ap-south-1.amazonaws.com/OTTImages/TheatresNearyou_w.png")
            ) {
                rv_ott!!.isFocusable = false
                enableOttFocus = false
                break
            } else {
                enableOttFocus = true
            }
        }
        rv_ott!!.adapter =
            OttModelAdapter(movieDetail!!.all_ott_models!!, context, enableOttFocus)
    }

    fun appNotFound(serviceName: String?) {
        val dialog = Dialog(this, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(this.resources.getColor(
                R.color.colorTransparent))
        )
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
            (this as Activity).finish()
        }
    }

    fun noActiveSubscription(serviceName: String?) {
        val dialog = Dialog(this, R.style.DialogTheme)
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(MainActivity.context.resources.getColor(
                R.color.colorTransparent))
        )
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
        dialogOk.setOnClickListener { (this as Activity).finish() }
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
        fun createIntentMovie(context: Context?, movieId : String , userId : Int): Intent {
            val intent = Intent(context, PlexigoOnContentDetailActivity::class.java)
            intent.putExtra("movieId", movieId)
            intent.putExtra("userId" , userId)
            return intent
        }
    }

    fun PlayInPlexigo() {
//        if (isFestivalMovie) {
//            fetchContentUrl()
//            return
//        }
//        if (isLive) {
//            PlayLiveEvent(DetailsFragment.movie.getVideopath(), 0)
//            return
//        }
        if (movieDetail?.ppv == "Y" && movieDetail?.inr != "0" && movieDetail?.inr != "-1"
        ) {
            CheckIfUserPpvSubscribed()
        } else {
            PlayVideo()
        }

//        if (movieDetail!!.ppvType.equals("Y") && !movieDetail!!.inr
//                .equals("0")
//        ) {
//            CheckIfUserPpvSubscribed()
//        } else {
//            PlayVideo()
//        }
    }


    override fun onOttClicked(model: OTTModel?) {
        if (CheckUserLogin.loadPrefs(this) == 1) {
            if(model!!.buttonIcon.equals("book" , ignoreCase = true)){
                PlayInOtherOtt(model.url,true)
            }else{
                if(model!!.ottPlatform.equals("Plexigo")){
                    PlayInPlexigo()
                }else{
                    PlayInOtherOtt(model.url,false)
                }
            }
//            if (model?.ottPlatform.equals("Plexigo")) {
//                if (movieDetail?.isLive.equals("1")) {
//                    isLive = true
//                }
//                if (movieDetail?.isFilmFestival.equals("y")) {
//                    isFestivalMovie = true
//                }
//                PlayInPlexigo()
//            } else {
//                //PlayInOtherOtt("https://www.hotstar.com/in/movies/laxmii/1260036200");
//                //  PlayInOtherOtt(model.getUrl())
//            }
        }else{
            startActivity(
                ProfileActivity.createIntentShow(
                    this
                )
            )
        }
    }

    override fun onItemSelected(item: Any?, index: Long) {
        if(item is TvSeriesEpisode){
            if(item?.overview?.length!! > 34){
                content_expand!!.visibility = View.VISIBLE
            }else{
                content_expand!!.visibility = View.GONE
            }
            item?.overview?.let { stringDecode(content_info, it) }
            item?.episodename?.let { stringDecode(content_name, it) }
        }

    }


}