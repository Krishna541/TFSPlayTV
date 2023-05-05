package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.activities.CommanActivity
import `in`.fiberstory.tfsplaytv.activities.DetailsActivity
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isMoviePlayable
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.DocumentariesResModel
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.presenter.CustomListRowPresenter
import `in`.fiberstory.tfsplaytv.presenter.MoviePresenter
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class MoviesFragment : BrowseSupportFragment() {
    private var customListRowPresenter: CustomListRowPresenter? = null
    private lateinit var navigationMenuCallback: NavigationMenuCallback
    private var mCategory: String = ""
     var count : Int  = 0

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        upFocus()
    }

    fun upFocus() {
        if (view != null) {
            val viewToFocusUp =
                activity?.findViewById<View>(`in`.fiberstory.tfsplaytv.R.id.horizontal_grid)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        setData()
        getSizeOfRow()
        initListeners()
        isHeadersTransitionOnBackEnabled = true
        headersState = HEADERS_DISABLED
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        val handler = Handler()
        handler.postDelayed({ startEntranceTransition() }, 500)
    }

    private fun getSizeOfRow() {
        Log.i("size", "getSizeOfRow: ${mRowsAdapter.size()}")
    }

    private fun setData() {
        allMovies

    }

    private fun initListeners() {
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = mRowsAdapter.indexOf(row)
                val indexOfItem =
                    ((row as ListRow).adapter as ArrayObjectAdapter).indexOf(item)

                itemViewHolder?.view?.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                if (indexOfItem == 0) {
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }

                            KeyEvent.KEYCODE_BACK -> {
                                if (indexOfItem != 0) {
                                    rowsSupportFragment.setSelectedPosition(
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
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }

                            KeyEvent.KEYCODE_DPAD_UP -> {
                                //pending
                                if (indexOfRow == 0) {
                                    rowsSupportFragment.setSelectedPosition(
                                        indexOfRow!!,
                                        true,
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
                                if (indexOfRow != null) {
                                    var count = mRowsAdapter!!.size() - 1
                                    if (indexOfRow >= count) {

                                        rowsSupportFragment.setSelectedPosition(
                                            indexOfRow!!,
                                            true,
                                            object : ListRowPresenter.SelectItemViewHolderTask(indexOfItem) {
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

/*
                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (indexOfRow == 5) {
                                    rowsSupportFragment.setSelectedPosition(
                                        indexOfRow,
                                        true,
                                        object :
                                            ListRowPresenter.SelectItemViewHolderTask(0) {
                                            override fun run(holder: Presenter.ViewHolder?) {
                                                super.run(holder)
                                                holder?.view?.postDelayed({
                                                    holder.view.requestFocus()
                                                }, 10)
                                            }
                                        })
                            }
                            }
                            */


                        }
                    }
                    false
                }


            }

        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                Log.e("view all", "clicked")

                var selectedMovieItem = item as DocumentaryItemsModel

                if (selectedMovieItem.isMore.equals("-1")
                ) {
                    activity?.startActivity(
                        CommanActivity.createIntentMovie(
                            activity,
                            selectedMovieItem.movieCategory,
                            0
                        )
                    )
                } else {
                    activity?.startActivity(
                        DetailsActivity.createIntentMovie(
                            activity,
                            selectedMovieItem
                        )
                    )
                }
            }
    }

    private fun setupAdapter() {
        customListRowPresenter = CustomListRowPresenter(activity)
        mRowsAdapter = ArrayObjectAdapter(customListRowPresenter)
        adapter = mRowsAdapter
    }

    override fun onStart() {
        super.onStart()
//        setupFocusSearchListener()
//        (activity?.findViewById<View>(R.id.title_tv) as TextView).visibility = View.GONE
    }


    private val allMovies: Unit
        get() {
            var index: Int
            for (m in 0 until mRowsAdapter.size()) {
                index = m + 1
                mRowsAdapter.removeItems(index, mRowsAdapter.size())
            }
            latestMovie

        }

    private val latestMovie: Unit
        get() {
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call: Call<String> = apiInterface.getLatestMovies(activity?.let {
                UserPreferences.getUserData(
                    it
                ).subscriberId
            }, "1")
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful) {
                            featuredMovie
                            try {
                                val gson = Gson()
                                val reader: Reader = StringReader(response.body().toString())
                                val movieModel: DocumentariesResModel =
                                    gson.fromJson(reader, DocumentariesResModel::class.java)
                                if (movieModel.status == 1) {
                                    isMoviePlayable = movieModel.playable
                                    mCategory = "latest"
                                    val moviePresenter =
                                        activity?.let { MoviePresenter(it, "latest") }
                                    val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                    for (movieDatumModel in movieModel.data!!) {
                                        arrayObjectAdapter.add(movieDatumModel)
                                    }
                                    if (arrayObjectAdapter.size() > 0) {
                                        if (movieModel.data.size > 10 || movieModel.data.size == 10) {
                                            arrayObjectAdapter.add(
                                                DocumentaryItemsModel(
                                                    "-1",
                                                    movieCategory = "latest"
                                                )
                                            )
                                        }
                                        val headerItem = HeaderItem(0, "Latest Movies")
                                        mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "You have not availed any movies yet",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
//                                    Toast.makeText(
//                                        activity,
//                                        "Latest: " + movieModel.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(
                            activity,
                            "Latest: " + exception.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            activity,
                            "Latest: " + exception.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        activity,
                        "Latest: " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    private val featuredMovie: Unit
        get() {
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call: Call<String> = apiInterface.getFeaturedMovies(activity?.let {
                UserPreferences.getUserData(
                    it
                ).subscriberId
            }, "1")
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful) {
                            getHindiMovie("\"" + "hindi" + "\"")
                            try {
                                val gson = Gson()
                                val reader: Reader = StringReader(response.body().toString())
                                val movieModel: DocumentariesResModel =
                                    gson.fromJson(reader, DocumentariesResModel::class.java)
                                if (movieModel.status == 1) {
                                    isMoviePlayable = movieModel.playable
                                    mCategory = "featured"
                                    val moviePresenter =
                                        activity?.let { MoviePresenter(it, "featured") }
                                    val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                    for (movieDatumModel in movieModel.data!!) {
                                        arrayObjectAdapter.add(movieDatumModel)
                                    }
                                    if (arrayObjectAdapter.size() > 0) {
                                        arrayObjectAdapter.add(
                                            DocumentaryItemsModel(
                                                "-1",
                                                movieCategory = "featured"
                                            )
                                        )
                                        val headerItem = HeaderItem(0, "Featured Movies")
                                        mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "You have not availed any movies yet",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Featured: " + movieModel.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(
                            activity,
                            "Featured: " + exception.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            activity,
                            "Featured: " + exception.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        activity,
                        "Featured: " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private fun getHindiMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getEnglishMovie("\"" + "english" + "\"")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Hindi: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "Hindi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Hindi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "Hindi: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getEnglishMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getMarathiMovie("\"" + "marathi" + "\"")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "English: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "English: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "English: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "English: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getMarathiMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTeluguMovie("\"" + "telugu" + "\"")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Marathi: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Marathi: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTeluguMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTamilMovie("\"" + "tamil" + "\"")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Telugu: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "Telugu: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Telugu: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "Telugu: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getTamilMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getMalayalamMovie("\"" + "malayalam" + "\"")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val movieModel: DocumentariesResModel =
                                gson.fromJson(reader, DocumentariesResModel::class.java)
                            if (movieModel.status == 1) {
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Tamil: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "Tamil: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Tamil: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "Tamil: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getMalayalamMovie(language: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getMovie(language, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
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
                                isMoviePlayable = movieModel.playable
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, language.replace("\"", "")) }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in movieModel.data!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    arrayObjectAdapter.add(
                                        DocumentaryItemsModel(
                                            "-1",
                                            movieCategory = language.replace("\"", "")
                                        )
                                    )
                                    val sb = StringBuilder(language.replace("\"", ""))
                                    sb.setCharAt(0, Character.toUpperCase(sb[0]))
                                    val headerItem = HeaderItem(0, "$sb Movies")
                                    mRowsAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Malayalam: " + movieModel.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "Malayalam: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Malayalam: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Malayalam: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        lateinit var mRowsAdapter: ArrayObjectAdapter
    }
}