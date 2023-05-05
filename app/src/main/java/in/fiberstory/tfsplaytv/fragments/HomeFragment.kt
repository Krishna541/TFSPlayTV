package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.*
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.appInstalledOrNot
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.appNotFound
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.launchAppUsingSSO
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.mPlayable
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.noActiveSubscription
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.*
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.network.APIInterface.youtubePlaylistGetContentBaseURL
import `in`.fiberstory.tfsplaytv.network.PlexigoAPIClient
import `in`.fiberstory.tfsplaytv.presenter.DefaultCardPresenterSelector
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import `in`.fiberstory.tfsplaytv.utility.userLoginDialogAlert
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import androidx.preference.PreferenceManager
import com.androijo.tvnavigation.utils.Constants
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import kotlin.properties.Delegates


@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : RowsSupportFragment() {

    private var mRowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(RowPresenterSelector())

    private lateinit var navigationMenuCallback: NavigationMenuCallback
    var bannerList = ArrayList<PromotionsItem>()
    var homePageHeaderTitle: String = ""
    var homePageHeaderType: String = ""
    var ItemClickFrom = ""
    private lateinit var selectedHomePageItem: Any
    private var PlexigoUserID by Delegates.notNull<Int>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var featuredContentId: String
    var youtubePlayListName: List<String> = ArrayList()
    var youtubePlayListData = ArrayList<YoutubePlayListResponseModel>()
//    var rowCount by Delegates.notNull<Int>()

    init {
        callHomeBannerAPI()
        initAdapters()
        initListeners()
    }

    private fun callHomePageListingAPI() {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)

        val apiCall: Call<String> = apiInterface.getHomePageListing(activity?.let {
            UserPreferences.getUserData(it).subscriberId
        })
        apiCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val homepageListModel: HomePageListingModel? =
                                gson.fromJson(reader, HomePageListingModel::class.java)
                            if (homepageListModel?.youtube_playlist != null) {
                                youtubePlayListName = homepageListModel.youtube_playlist
                                getYoutubePlaylistItemsVideo()
                            }
                            if (homepageListModel?.status == 1) {
                                if (homepageListModel.data?.isNotEmpty() == true) {
                                    MainActivity.isMoviePlayable = homepageListModel.playable!!
                                    MainActivity.isLiveTVPlayable = homepageListModel.playable!!

                                    mPlayable = homepageListModel.playable
                                    ItemClickFrom = "Home"
                                    setHomePageList(
                                        homePageHeaderTitle, homepageListModel.data
                                    )
                                }


                                if (sharedPreferences.getString("isNewMovieBannerClicked", "")
                                        .equals("", ignoreCase = true)
                                ) {
//                                    dialogNewMovie(
//                                        homepageListModel.featured_content_image,
//                                        homepageListModel.featured_content_id
//                                    )


                                }


                            }

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            activity, "" + response.message().toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Toast.makeText(activity, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getYoutubePlaylistItemsVideo() {
        var API_KEY: String? = ""
        try {
            API_KEY = requireActivity().packageManager.getApplicationInfo(requireActivity().packageName, PackageManager.GET_META_DATA).metaData.getString("com.my.app.myYoutubeID")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        for (i in youtubePlayListName) {
            val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
            val apiCall: Call<String> = apiInterface.getYoutubePlayListVideoItems(youtubePlaylistGetContentBaseURL,"snippet", i, "date", 10, null,
                API_KEY)
            apiCall.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful) {
                            try {
                                val gson = Gson()
                                val reader: Reader = StringReader(response.body().toString())
                                val homepageListModel: YoutubePlayListResponseModel? =
                                    gson.fromJson(reader, YoutubePlayListResponseModel::class.java)
                                youtubePlayListData.addAll(listOf(homepageListModel!!))
                                if (youtubePlayListData.size == youtubePlayListName.size) { setHomePageYoutubeList(youtubePlayListData!!) }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(activity, "" + response.message().toString(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(activity, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
    fun dialogNewMovie(img: String, featuredContentId: String) {
        val dialog = context?.let { Dialog(it, R.style.DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_new_movie)
        dialog?.window!!.setBackgroundDrawable(
            context?.resources?.getColor(R.color.colorTransparent)?.let { ColorDrawable(it) }
        )
        dialog.show()

        val imageUrl = dialog.findViewById<ImageView>(R.id.img_ad_movie_banner)
        val cross = dialog.findViewById<ImageView>(R.id.img_close)
        Glide.with(requireActivity()).load(img).into(imageUrl)
        imageUrl.setOnClickListener {
            if (CheckUserLogin.loadPrefs(requireActivity().applicationContext) === 1) {
                editor = sharedPreferences.edit()
                editor.putString("isNewMovieBannerClicked", "true")
                editor.apply()
                startActivity(
                    PlexigoOnContentDetailActivity.createIntentMovie(
                        context, featuredContentId, userId = PlexigoUserID
                    )
                )
                dialog.dismiss()
            } else {
                requireActivity().userLoginDialogAlert(requireActivity())
            }

        }
        imageUrl!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP) {
                when (i) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        cross!!.requestFocus()
                        cross.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(), com.androijo.tvnavigation.R.color.orange
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        );
                        imageUrl!!.clearFocus()
                        return@OnKeyListener false
                    }
                }
            }


            false
        })
        cross!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        imageUrl!!.requestFocus()
                        cross!!.clearFocus()
                        cross.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(), com.androijo.tvnavigation.R.color.white
                            ), android.graphics.PorterDuff.Mode.MULTIPLY
                        );
                        return@OnKeyListener false
                    }
                }
            }


            false
        })
        cross.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun setHomePageList(
        homePageHeaderTitle: String, homePageList: ArrayList<DataItem>
    ) {
//        rowCount = homePageList.size
        for (i in 0 until homePageList.size) {
            mRowsAdapter.add(homePageList[i].data?.let {
                homePageHeaderType = homePageList[i].type
                createHomeCardRow(
                    homePageList[i].title, it
                )
            })
        }
    }

    private fun createHomeCardRow(
        homePageHeaderTitle: String, homePageList: java.util.ArrayList<HomeModel>
    ): Any {
        val presenterSelector = activity?.baseContext?.let {
            DefaultCardPresenterSelector(it, "Home")
        }
        val adapter = ArrayObjectAdapter(presenterSelector)
        val headerItem = HeaderItem(homePageHeaderTitle)
        for (data in homePageList) {
            adapter.add(data)
        }

        return BannerCardListRow(headerItem, adapter)
    }


    private fun setHomePageYoutubeList(homePageList: ArrayList<YoutubePlayListResponseModel>) {
        for (i in 0 until homePageList.size) {
            mRowsAdapter.add(1, homePageList.get(i).items.let {
                homePageHeaderType = homePageList[i].items?.get(i)?.snippet?.channelTitle.toString()
                createHomeYoutubeCardRow(
                    homePageList[i].items?.get(i)?.snippet?.channelTitle.toString(),
                    it as java.util.ArrayList<Item>
                )
            })
        }
    }

    private fun createHomeYoutubeCardRow(homePageHeaderTitle: String, homePageList: java.util.ArrayList<Item>): Any {
        val presenterSelector = activity?.baseContext?.let {
            DefaultCardPresenterSelector(it, "Youtube")
        }
        val adapter = ArrayObjectAdapter(presenterSelector)
        val headerItem = HeaderItem(homePageHeaderTitle)
        for (data in homePageList) {
            adapter.add(data)
        }
        if (adapter.size() > 9) {
            adapter.add(Item(isMore = "-1", content_type =  getString(R.string.youtube)))
        }
        return BannerCardListRow(headerItem, adapter)
    }

    private fun callHomeBannerAPI() {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)

        val apiCall: Call<String> = apiInterface.getPromotion(activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        apiCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
//                progressbarManager.dismiss()
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())

                            val homepageBannerModel: BannerModel =
                                gson.fromJson(reader, BannerModel::class.java)
                            if (homepageBannerModel.status == 1) {
                                ItemClickFrom = "Banner"
                                if (homepageBannerModel.promotions?.isNotEmpty() == true) {
                                    bannerList = homepageBannerModel.promotions
                                    setBanner(bannerList)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            activity, "" + response.message().toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setBanner(bannerList: ArrayList<PromotionsItem>) {
        mRowsAdapter.add(
            createCardRow(
                bannerList
            )
        )
    }

    private fun createCardRow(get: ArrayList<PromotionsItem>): Row {
        val presenterSelector = activity?.baseContext?.let {
            DefaultCardPresenterSelector(it, "Banner")
        }

        val headerItem = HeaderItem("")
        val adapter = ArrayObjectAdapter(presenterSelector)
        for (data in get) {
            adapter.add(data)
        }

        return BannerCardListRow(headerItem, adapter)
    }

    private fun initAdapters() {
        adapter = mRowsAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = mRowsAdapter.indexOf(row)

                val indexOfItem = ((row as ListRow).adapter as ArrayObjectAdapter).indexOf(item)

                itemViewHolder?.view?.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                if (indexOfItem == 0) {
                                    Constants.backCount = 1
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }

                            KeyEvent.KEYCODE_BACK -> {
                                if (indexOfItem != 0) {
                                    setSelectedPosition(
                                        indexOfRow,
                                        true,
                                        object : ListRowPresenter.SelectItemViewHolderTask(0) {
                                            override fun run(holder: Presenter.ViewHolder?) {
                                                super.run(holder)
                                                holder?.view?.postDelayed({
                                                    holder.view.requestFocus()
                                                }, 10)
                                            }
                                        })
                                } else {
                                    Constants.backCount = 0
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }

                            KeyEvent.KEYCODE_DPAD_UP -> {
                                if (indexOfRow == 0) {
                                    setSelectedPosition(indexOfRow, true,
                                        object : ListRowPresenter.SelectItemViewHolderTask(
                                            indexOfItem
                                        ) {
                                            override fun run(holder: Presenter.ViewHolder?) {
                                                super.run(holder)
                                                holder?.view?.postDelayed({
                                                    holder.view.requestFocus()
                                                }, 10)
                                            }
                                        })

                                }
                            }

                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (indexOfRow >= mRowsAdapter.size() - 1) {
                                    setSelectedPosition(indexOfRow, true,
                                        object : ListRowPresenter.SelectItemViewHolderTask(
                                            indexOfItem
                                        ) {
                                            override fun run(holder: Presenter.ViewHolder?) {
                                                super.run(holder)
                                                holder?.view?.postDelayed({
                                                    holder.view.requestFocus()
                                                }, 10)
                                            }
                                        })

                                }
                            }
                        }
                    }
                    false
                }
            }



        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = mRowsAdapter.indexOf(row)
                if (item.toString().contains("isFrom")) {
                    var selectedBannerItem = item as PromotionsItem
                    if (CheckUserLogin.loadPrefs(activity) != 1) {
                        activity?.userLoginDialogAlert(requireContext())
                    } else {
                        if (item.content_url_type.equals("rental", ignoreCase = true)) {
                            startActivity(
                                PlexigoOnContentDetailActivity.createIntentMovie(
                                    context, selectedBannerItem.content_id, userId = PlexigoUserID
                                )
                            )
                        } else if (item.content_url_type.equals("")) {

                        } else {
                            if (appInstalledOrNot(selectedBannerItem.apk_package_id)) {
                                if (mPlayable == 1) {
                                    //launchApp(movieDetail.getIs_leanback(), movieDetail.getDeeplink_url_tv(), movieDetail.getApkPackageId());
                                    launchAppUsingSSO(
                                        selectedBannerItem.content_url,
                                        selectedBannerItem.ott_service_name,
                                        selectedBannerItem.apk_package_id
                                    )
                                } else {
                                    noActiveSubscription(selectedBannerItem.ott_service_name)
                                }
                            } else {
                                appNotFound(selectedBannerItem.ott_service_name)
                            }
                        }

                    }
                }
                else if (item.toString().contains("kind")) {
                    var selectedHomePageItem = item as Item
                    if (selectedHomePageItem.isMore == "-1") {
                        val youtubePlaylistArray = youtubePlayListData.reversed()
                        val playlist: String = youtubePlaylistArray.get(indexOfRow - 1).items?.get(0)?.snippet?.playlistId.toString()
                        activity?.startActivity(CommanActivity.youtubePlayListId(context, "youtube", playlist, 105))
                    } else {
                        val YoutubePLaylistIntent = Intent(Intent.ACTION_VIEW, Uri.parse(APIInterface.youtubeBaseURI + selectedHomePageItem.snippet?.resourceId?.videoId))
                        try {
                            activity?.startActivity(YoutubePLaylistIntent)
                        } catch (ex: ActivityNotFoundException) {
                            activity?.startActivity(YoutubePLaylistIntent)
                        }
                    }
                } else {
                    var selectedHomePageItem = item as HomeModel

                    if (selectedHomePageItem.content_type.equals("live")) {
                        if (CheckUserLogin.loadPrefs(activity) != 1) {
                            activity?.userLoginDialogAlert(requireContext())

                        } else {
                            if (appInstalledOrNot(selectedHomePageItem.apkPackageId)) {
                                if (MainActivity.isLiveTVPlayable == 1) {
                                    launchAppUsingSSO(
                                        selectedHomePageItem.deeplinkUrlTv,
                                        selectedHomePageItem.ottServiceName,
                                        selectedHomePageItem.apkPackageId
                                    )

                                } else {
                                    noActiveSubscription(
                                        selectedHomePageItem.ottServiceName
                                    )
                                }
                            } else {
                                appNotFound(selectedHomePageItem.ottServiceName)
                            }
                        }
                    } else if(selectedHomePageItem.content_type.equals("youtube")){
                        val youtubeChannelIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(selectedHomePageItem.channel_url)
                        )
                        try {
                            activity?.startActivity(youtubeChannelIntent)
                        } catch (ex: ActivityNotFoundException) {
                            activity?.startActivity(youtubeChannelIntent)
                        }

                    }else if(selectedHomePageItem.content_type.equals("linear")){
                        activity?.startActivity(
                            ExoplayerActivity.exoplayerMovieContent(
                                activity,
                                selectedHomePageItem.channel_url!!
                            )
                        )
                    }
                    else if (selectedHomePageItem.content_type.equals("show")) {
                        activity?.startActivity(
                            EpisodeActivity.createIntentShow(
                                activity,
                                selectedHomePageItem.show_id,
                                selectedHomePageItem.show_name
                            )
                        )
                    } else {
                        var documentaryModel = DocumentaryItemsModel(
                            ottServiceName = selectedHomePageItem.ottServiceName,
                            movieInfo = selectedHomePageItem.movieInfo,
                            movieName = selectedHomePageItem.movieName,
                            castAndCrew = selectedHomePageItem.castAndCrew,
                            genre = selectedHomePageItem.genre1,
                            genre1 = selectedHomePageItem.genre2,
                            genre2 = selectedHomePageItem.genre3,
                            duration = selectedHomePageItem.duration,
                            censorRating = selectedHomePageItem.censorRating,
                            releaseYear = selectedHomePageItem.releaseYear,
                            detailPageCoverTv = selectedHomePageItem.detailPageCoverTv,
                            apkPackageId = selectedHomePageItem.apkPackageId,
                            deeplinkUrlTv = selectedHomePageItem.deeplinkUrlTv
                        )
                        activity?.startActivity(
                            DetailsActivity.createIntentMovie(
                                activity, documentaryModel
                            )
                        )
                    }

                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        upFocus()
    }

    private fun checkUserExistAPI() {
        val apiInterface: APIInterface = PlexigoAPIClient.getClient("", APIInterface.DOMAIN_URL)!!
            .create(APIInterface::class.java)


        val req = PlexigoChannelReqModel(activity?.let { UserPreferences.getUserData(it).mobileNo })

        val plexigoApiCall = apiInterface.checkUserExistOrNot(req)

        plexigoApiCall.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>, response: Response<String>
            ) {

                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val userModel: PlexigoUserExistApiResponseModel = gson.fromJson(
                                reader, PlexigoUserExistApiResponseModel::class.java
                            )

                            if (userModel.status == "Success") {

                                PlexigoUserID = userModel.data?.user?.get(0)?.userId!!
                                editor = sharedPreferences.edit()
                                editor.putInt("PlexigoUserId", PlexigoUserID)
                                editor.apply()
                            } else if (userModel.status != null && userModel.status.equals("Failure")) {
                                plexigoUserRegistration()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show()
                    }
                } catch (exception: java.lang.IllegalStateException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }


            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireActivity(), "" + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun plexigoUserRegistration() {

        val apiInterface: APIInterface = PlexigoAPIClient.getClient("", APIInterface.DOMAIN_URL)!!
            .create(APIInterface::class.java)

        val req = PlexigoDoRegisterRequestModel(
            activity?.let { UserPreferences.getUserData(it).name },
            activity?.let { UserPreferences.getUserData(it).mobileNo },
            activity?.let { UserPreferences.getUserData(it).email },
            ""
        )


        val plexigoApiCall = apiInterface.doPlexigoUserRegistration(req)

        plexigoApiCall.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val userModel = gson.fromJson(
                                reader, PlexigoUserExistApiResponseModel::class.java
                            )
                            if (userModel.status != null && userModel.status.equals("Success")) {
                                PlexigoUserID = userModel.data?.user?.get(0)?.userId!!
                            } else if (userModel.status != null && userModel.status.equals("Failure")) {
                                Toast.makeText(
                                    activity,
                                    "" + userModel.data?.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            activity, "" + response.message().toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: java.lang.IllegalStateException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(activity, "" + exception.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Toast.makeText(requireActivity(), "" + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkUserExistAPI()
        callHomePageListingAPI()
        restoreSelection()
    }

    fun upFocus() {
        if (view != null) {
            val viewToFocusUp = activity?.findViewById<View>(R.id.horizontal_grid)
            viewToFocusUp?.requestFocus()
            val browseFrameLayout: BrowseFrameLayout? =
                view?.findViewById(androidx.leanback.R.id.browse_frame)
            browseFrameLayout?.onFocusSearchListener =
                BrowseFrameLayout.OnFocusSearchListener { focused, direction ->
                    if (direction == View.FOCUS_UP) {
                        null
                    } else {
                        null
                    }
                }
        }
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    /**
     * this function can put focus or select a specific item in a specific row
     */
    fun restoreSelection() {
        setSelectedPosition(0, true, object : ListRowPresenter.SelectItemViewHolderTask(0) {
            override fun run(holder: Presenter.ViewHolder?) {
                super.run(holder)
                holder?.view?.postDelayed({
                    holder.view.requestFocus()
                }, 10)
            }
        })
    }

}
