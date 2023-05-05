package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.model.*
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.network.APIInterface.youtubePlaylistGetContentBaseURL
import `in`.fiberstory.tfsplaytv.network.PlexigoAPIClient
import `in`.fiberstory.tfsplaytv.pagination.PaginationAdapter
import `in`.fiberstory.tfsplaytv.pagination.PostAdapter
import `in`.fiberstory.tfsplaytv.presenter.CustomVerticalGridPresenter
import `in`.fiberstory.tfsplaytv.presenter.MoviePresenter
import `in`.fiberstory.tfsplaytv.presenter.OnRentmoviePresenter
import `in`.fiberstory.tfsplaytv.presenter.TVShowsPresenter
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.FocusHighlight
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class CommanFragment : VerticalGridSupportFragment() {
    private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE
    var bundle: Bundle? = null
    private var category: String? = null
    private var apiInterface: APIInterface? = null
    private var responseFailed = false
    private var postAdapter: PostAdapter? = null
    private var getModeValue: Int? = 0
    private var channelId: Int? = 0
    private var userId: Int? = 0
    private var playlistId: String? = ""
//    var PageCount = 1
    var youtubePageCount = 10
    var youtubePlayListData = ArrayList<YoutubePlayListResponseModel>()
    private var nextPageToken: String? = null
    var isYoutubeColumns: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setupAdapter()
        setData()
        setupAdapter()

        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        val handler = Handler()
        handler.postDelayed({ startEntranceTransition() }, 500)
    }

    override fun onStart() {
        super.onStart()
        val sb = StringBuilder(category)
        sb.setCharAt(0, sb[0].uppercaseChar())
        var tvShowsName = ""
        if (category.equals("originals", ignoreCase = true)) tvShowsName = "Originals"
        if (category.equals("world", ignoreCase = true)) tvShowsName = "From Around The World"
        if (category.equals("set", ignoreCase = true)) tvShowsName = "Shows On Set"
        if (category.equals("sab", ignoreCase = true)) tvShowsName = "Shows on SAB"
        if (category.equals("sonymarathi", ignoreCase = true)) tvShowsName = "Shows on Sony Marathi"
        if (category.equals("bangla", ignoreCase = true)) tvShowsName = "Shows on Sony Aath"
        if (category.equals("bbcearth", ignoreCase = true)) tvShowsName = "Shows on Sony BBC Earth"
        if (category.equals("tvfplay", ignoreCase = true)) tvShowsName = "TVF Play"
        if (category.equals("onRentMovie", ignoreCase = true)) tvShowsName = "OnRent Movie"

//        (activity?.findViewById<View>(R.id.horizontal_grid) as HorizontalGridView).visibility =
//            View.GONE
//        (activity?.findViewById<View>(R.id.title_tv) as TextView).text =
//            if (comeFrom == 0) "$sb Movies" else tvShowsName
    }

    private fun setData() {
        bundle = activity?.intent?.extras
        if (bundle != null) {
            category = bundle?.getString("CATEGORY")
            getModeValue = bundle?.getInt("MODE", 0)
            channelId = bundle?.getInt("ChannelId", 0)
            userId = bundle?.getInt("PlexigoUserId", 0)
            playlistId = bundle?.getString("PLAYLISTID", "")
            postAdapter = if (getModeValue == 0) PostAdapter(
                activity,
                activity?.let { category?.let { it1 -> MoviePresenter(it, it1) } },
                category
            ) else if (getModeValue == 101) PostAdapter(
                activity,
                activity?.let { category?.let { it1 -> OnRentmoviePresenter(it, it1) } },
                category
            )
            else if (getModeValue == 105) PostAdapter(
                activity,
                activity?.let { category?.let { it1 -> HomePageYoutubePresenter(it, 0) } },
                category
            )
            else PostAdapter(
                activity,
                activity?.let { category?.let { it1 -> TVShowsPresenter(it, it1) } },
                category
            )
            adapter = postAdapter
            if (getModeValue == 0) {
                if (category.equals("latest", ignoreCase = true)) {
                    latestMovie
                } else if (category.equals("featured", ignoreCase = true)) {
                    featuredMovie
                } else {
                    getMovie(category)
                }
            } else if (getModeValue == 101) {
                onRentMovie()
            } else if (getModeValue == 105) {
                fetchMoreYoutubeContent()
            } else {
                getTVShows(category)
            }
        }
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is DocumentaryItemsModel) {

                // If any item on the bottom row is selected...
                if (getModeValue == 0) {
                    val itemIndex: Int? = postAdapter?.indexOf(item)
                    val minimumIndex: Int? = postAdapter?.allMovieItems?.size?.minus(COLUMNS)
                    if (itemIndex != null) {
                        if (itemIndex >= minimumIndex!! && postAdapter?.shouldLoadNextPage() == true) {
                            if (category.equals("latest", ignoreCase = true)) {
                                latestMovie
                            } else if (category.equals("featured", ignoreCase = true)) {
                                featuredMovie
                            } else {
                                getMovie(category)
                            }
                        }
                    }
                }
            } else if (item is Content) {
                val itemIndex: Int? = postAdapter?.indexOf(item)
                val minimumIndex: Int? = postAdapter?.getAllContentByChannel()?.size?.minus(COLUMNS)
                if (itemIndex != null) {
                    if (itemIndex >= minimumIndex!! && postAdapter?.shouldLoadNextPage() == true && PageCount != 1) {
                        onRentMovie()
                    }
                }
            } else if (item is TVShowsDatumModel) {
                val itemIndex: Int? = postAdapter?.indexOf(item)
                val minimumIndex: Int? = postAdapter?.getAllTVShowsItems()?.size?.minus(COLUMNS)
                if (itemIndex != null) {
                    if (itemIndex >= minimumIndex!! && postAdapter?.shouldLoadNextPage() == true) {
                        getTVShows(category)
                    }
                }
            } else if (item is Item) {
                val itemIndex: Int? = postAdapter?.indexOf(item)
                val minimumIndex: Int? =
                    postAdapter?.getAllYoutubePlaylistContent()?.size?.minus(YOUTUBECOLUMNS)
                if (itemIndex != null) {
                    if (itemIndex >= minimumIndex!! && postAdapter?.shouldLoadNextPage() == true && nextPageToken!= null ) {
                        fetchMoreYoutubeContent()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        val presenter = CustomVerticalGridPresenter(ZOOM_FACTOR, false)
        if (isYoutubeColumns) {
            presenter.numberOfColumns = YOUTUBECOLUMNS

        } else {
            presenter.numberOfColumns = COLUMNS
        }
        presenter.shadowEnabled = false
        gridPresenter = presenter
        setSelectedPosition(0)
    }

    private val latestMovie: Unit
        private get() {
            if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
            val options: Map<String, String> = postAdapter?.adapterOptions as Map<String, String>
            val tag = options[PaginationAdapter.KEY_TAG]
            val anchor = options[PaginationAdapter.KEY_ANCHOR]
            val nextPage = options[PaginationAdapter.KEY_NEXT_PAGE]
            apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call: Call<String> = apiInterface?.getLatestMovies(
                activity?.let { UserPreferences.getUserData(it).subscriberId },
                nextPage
            ) as Call<String>
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful) {
                            try {
                                val gson = Gson()
                                val reader: Reader = StringReader(response.body().toString())
                                val movieModel: DocumentariesResModel =
                                    gson.fromJson(reader, DocumentariesResModel::class.java)
                                if (movieModel.status == 1) {
                                    Handler().postDelayed({
                                        if (postAdapter?.size() == 0 && movieModel.data?.isEmpty() == true) {
                                            postAdapter?.showReloadCard()
                                        } else {
                                            if (anchor == null) {
                                            }
                                            postAdapter?.setNextPage(
                                                movieModel.nextPage
                                            )
                                            postAdapter?.addAllMovieItems(movieModel.data)
                                            postAdapter?.removeLoadingIndicator()
                                        }
                                    }, 500)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (exception: IllegalStateException) {
                        responseFailed = true
                    } catch (exception: JsonSyntaxException) {
                        responseFailed = true
                    }
                    if (responseFailed) {
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })
        }
    private val featuredMovie: Unit
        private get() {
            if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
            val options: Map<String, String> = postAdapter?.adapterOptions as Map<String, String>
            val tag = options[PaginationAdapter.KEY_TAG]
            val anchor = options[PaginationAdapter.KEY_ANCHOR]
            val nextPage = options[PaginationAdapter.KEY_NEXT_PAGE]
            apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call: Call<String>? = apiInterface?.getFeaturedMovies(
                activity?.let { UserPreferences.getUserData(it).subscriberId },
                nextPage
            )
            call?.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful) {
                            try {
                                val gson = Gson()
                                val reader: Reader = StringReader(response.body().toString())
                                val movieModel: DocumentariesResModel =
                                    gson.fromJson(reader, DocumentariesResModel::class.java)
                                if (movieModel.status == 1) {
                                    Handler().postDelayed({
                                        if (postAdapter?.size() == 0 && movieModel?.data?.isEmpty() == true) {
                                            postAdapter?.showReloadCard()
                                        } else {
                                            if (anchor == null) {
                                            }
                                            postAdapter?.setNextPage(
                                                movieModel.nextPage
                                            )
                                            postAdapter?.addAllMovieItems(movieModel.data)
                                            postAdapter?.removeLoadingIndicator()
                                        }
                                    }, 500)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (exception: IllegalStateException) {
                        responseFailed = true
                    } catch (exception: JsonSyntaxException) {
                        responseFailed = true
                    }
                    if (responseFailed) {
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })
        }

    private fun getMovie(language: String?) {
        if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
        val options: Map<String, String> = postAdapter?.adapterOptions as Map<String, String>
        val tag = options[PaginationAdapter.KEY_TAG]
        val anchor = options[PaginationAdapter.KEY_ANCHOR]
        val nextPage = options[PaginationAdapter.KEY_NEXT_PAGE]
        apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> =
            apiInterface?.getMovie(
                "\"" + language + "\"",
                nextPage,
                activity?.let { UserPreferences.getUserData(it).subscriberId }) as Call<String>
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                Handler().postDelayed({
                                    if (postAdapter?.size() == 0 && movieModel?.data?.isEmpty() == true) {
                                        postAdapter?.showReloadCard()
                                    } else {
                                        if (anchor == null) {
                                            anchor?.let { postAdapter?.setAnchor(it) }
                                        }
                                        postAdapter?.setNextPage(movieModel.nextPage)
                                        postAdapter?.addAllMovieItems(movieModel.data)
                                        postAdapter?.removeLoadingIndicator()
                                    }
                                }, 500)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    responseFailed = true
                } catch (exception: JsonSyntaxException) {
                    responseFailed = true
                }
                if (responseFailed) {
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                postAdapter?.removeLoadingIndicator()
                if (postAdapter?.size() == 0) {
                    postAdapter?.showTryAgainCard()
                } else {
                    Toast.makeText(
                        activity, "Sorry, there was an error loading more data", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getTVShows(category: String?) {
        if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
        val options: Map<String, String> = postAdapter?.adapterOptions as Map<String, String>
        val tag = options[PaginationAdapter.KEY_TAG]
        val anchor = options[PaginationAdapter.KEY_ANCHOR]
        val nextPage = options[PaginationAdapter.KEY_NEXT_PAGE]
        apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface?.getTVShows(
            category,
            nextPage,
            activity?.let { UserPreferences.getUserData(it).subscriberId }) as Call<String>
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                Handler().postDelayed({
                                    if (postAdapter?.size() == 0 && tvShowsModel.data?.isEmpty() == true) {
                                        postAdapter?.showReloadCard()
                                    } else {
                                        if (anchor == null) {
                                        }
                                        postAdapter?.setNextPage(tvShowsModel.nextPage)
                                        postAdapter?.addAllTVShowsItems(tvShowsModel.data)
                                        postAdapter?.removeLoadingIndicator()
                                    }
                                }, 500)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    responseFailed = true
                } catch (exception: JsonSyntaxException) {
                    responseFailed = true
                }
                if (responseFailed) {
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                postAdapter?.removeLoadingIndicator()
                if (postAdapter?.size() == 0) {
                    postAdapter?.showTryAgainCard()
                } else {
                    Toast.makeText(
                        activity, "Sorry, there was an error loading more data", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun onRentMovie() {
        if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
        val options: Map<String, String> = postAdapter?.adapterOptions as Map<String, String>
        val tag = options[PaginationAdapter.KEY_TAG]
        val anchor = options[PaginationAdapter.KEY_ANCHOR]
        val nextPage = options[PaginationAdapter.KEY_NEXT_PAGE]
        apiInterface = PlexigoAPIClient.getClient("1.1", APIInterface.DOMAIN_URL)!!
            .create(APIInterface::class.java)
        val call: Call<String> = apiInterface!!.getContentByChannelAPI(
            channelId!!,
            PageCount,
            10,
            userId!!
        ) as Call<String>
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val contentModel: ContentByChannelModel =
                                gson.fromJson(reader, ContentByChannelModel::class.java)
                            if (contentModel.status == "success") {
                                Handler().postDelayed({
                                    if (postAdapter?.size() == 0) {
                                        postAdapter?.showReloadCard()
                                    } else {
                                        if (anchor == null) {
                                        }
                                        if (contentModel.data.pageCount != PageCount) {
                                            PageCount++
                                        } else {
                                            PageCount = 1
                                        }
                                        postAdapter?.setNextPage(PageCount)
                                        postAdapter?.addAllContentByChannel(contentModel.data.content)
                                        postAdapter?.removeLoadingIndicator()
                                    }
                                }, 500)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    responseFailed = true
                } catch (exception: JsonSyntaxException) {
                    responseFailed = true
                }
                if (responseFailed) {
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                postAdapter?.removeLoadingIndicator()
                if (postAdapter?.size() == 0) {
                    postAdapter?.showTryAgainCard()
                } else {
                    Toast.makeText(
                        activity, "Sorry, there was an error loading more data", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


    private fun fetchMoreYoutubeContent() {

        var API_KEY: String? = ""
        if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
        isYoutubeColumns = true
        try {
            API_KEY = requireActivity().packageManager.getApplicationInfo(
                requireActivity().packageName,
                PackageManager.GET_META_DATA
            ).metaData.getString("com.my.app.myYoutubeID")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val apiInterface: APIInterface =
            APIClient.getClient().create(APIInterface::class.java)

        val apiCall: Call<String> =
            apiInterface.getYoutubePlayListVideoItems(youtubePlaylistGetContentBaseURL,"snippet", playlistId, "date", 10, nextPageToken,
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
                            nextPageToken = homepageListModel?.nextPageToken
                            youtubePlayListData.addAll(listOf(homepageListModel!!))
                            postAdapter?.addAllYoutubePlaylistContent(homepageListModel.items)

                            if (homepageListModel.pageInfo?.totalResults!! >= youtubePageCount) {
                                PageCount + 10
                            } else {
                                PageCount = 10
                            }
                            postAdapter?.setNextPage(PageCount)
                            postAdapter?.removeLoadingIndicator()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            activity, "" + response.message().toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    companion object {
        private const val COLUMNS = 6
        private const val YOUTUBECOLUMNS = 3
        private var PageCount = 1

    }
}