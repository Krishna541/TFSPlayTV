package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.activities.CommanActivity
import `in`.fiberstory.tfsplaytv.activities.EpisodeActivity
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isTVShowsPlayable
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.TVShowsDatumModel
import `in`.fiberstory.tfsplaytv.model.TVShowsResModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.presenter.CustomListRowPresenter
import `in`.fiberstory.tfsplaytv.presenter.TVShowsPresenter
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.androijo.tvnavigation.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import kotlin.properties.Delegates

class TVShowsFragment : BrowseSupportFragment() {
    private lateinit var navigationMenuCallback: NavigationMenuCallback
    private var customListRowPresenter: CustomListRowPresenter? = null
    private var tvShowsCategory: String = ""
    override fun onStart() {
        super.onStart()
//        (activity!!.findViewById<View>(R.id.title_tv) as TextView).visibility =
//            View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isHeadersTransitionOnBackEnabled = true
        headersState = HEADERS_DISABLED
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        val handler = Handler()
        handler.postDelayed({ startEntranceTransition() }, 500)
        setUpAdapter()
        allTVShows
        initListeners()
    }

    private fun setUpAdapter() {
        customListRowPresenter = CustomListRowPresenter(activity)
        mRowsAdapter = ArrayObjectAdapter(customListRowPresenter)
        adapter = mRowsAdapter
    }

    private fun initListeners() {
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = mRowsAdapter?.indexOf(row)

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
                                if(indexOfItem != 0){
                                    rowsSupportFragment.setSelectedPosition(indexOfRow!!, true, object : ListRowPresenter.SelectItemViewHolderTask(0) {
                                        override fun run(holder: Presenter.ViewHolder?) {
                                            super.run(holder)
                                            holder?.view?.postDelayed({
                                                holder.view.requestFocus()
                                            }, 10)
                                        }
                                    })
                                }else{
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }

                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (indexOfRow != null) {
                                    var count = mRowsAdapter!!.size()-1
                                    if(indexOfRow >= count) {

                                        rowsSupportFragment.setSelectedPosition(
                                            indexOfRow!!,
                                            true,
                                            object : ListRowPresenter.SelectItemViewHolderTask(0) {
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
                    }
                    false
                }
            }

        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                Log.e("view all", "clicked")

                var selectedMovieItem = item as TVShowsDatumModel

                if (selectedMovieItem.id == "-1"
                ) {
                    activity?.startActivity(
                        CommanActivity.createIntentMovie(
                            activity,
                            selectedMovieItem.showCategory,
                            1
                        )
                    )
                } else {
                    activity?.startActivity(
                        EpisodeActivity.createIntentShow(
                            activity,
                            selectedMovieItem.showId,
                            selectedMovieItem.showName
                        )
                    )
                }
            }
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    private val allTVShows: Unit
        get() {
            var index: Int
            for (m in 0 until mRowsAdapter!!.size()) {
                index = m + 1
                mRowsAdapter!!.removeItems(index, mRowsAdapter!!.size())
            }
            getTVShowsOriginals("originals")
        }

    private fun getTVShowsOriginals(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsWorld("world")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                isTVShowsPlayable = tvShowsModel.playable
                                tvShowsCategory = category
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 ) {

                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Originals")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Originals: " + tvShowsModel.message,
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
                        "Originals: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Originals: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "Originals: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getTVShowsWorld(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsSet("set")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 ) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "From Around The World")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))

                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "From Around The World: " + tvShowsModel.message,
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
                        "From Around The World: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "From Around The World: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "From Around The World: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsSet(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getZeeHindiShows("zee5hindi")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {

                                    if (tvShowsModel.data.size > 9 ) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows On Set")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))

                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Shows On Set: " + tvShowsModel.message,
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
                        "Shows On Set: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Shows On Set: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Shows On Set: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsSab(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsEpicon("epicon")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 ) {

                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows on SAB")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Shows on SAB: " + tvShowsModel.message,
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
                        "Shows on SAB: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Shows on SAB: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Shows on SAB: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getZeeHindiShows(category: String) {
        val apiInterface = APIClient.getClient().create<APIInterface>(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getZeeMarathiShows("zee5marathi")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "ZEE5 Shows in Hindi")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "ZEE5 Shows in Hindi: " + tvShowsModel.message,
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
                        "ZEE5 Shows in Hindi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "ZEE5 Shows in Hindi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "ZEE5 Shows in Hindi: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getZeeMarathiShows(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsSab("sab")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "ZEE5 Shows in Marathi")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "ZEE5 Shows in Marathi: " + tvShowsModel.message,
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
                        "ZEE5 Shows in Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "ZEE5 Shows in Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "ZEE5 Shows in Marathi: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsSonyMarathi(category: String) {
        val apiInterface = APIClient.getClient().create<APIInterface>(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsBangla("bangla")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows on Sony Marathi")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Shows on Sony Marathi: " + tvShowsModel.message,
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
                        "Shows on Sony Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Shows on Sony Marathi: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Shows on Sony Marathi: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsBangla(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsBbcEarth("bbcearth")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows on Sony Aath")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Shows on Sony Aath: " + tvShowsModel.message,
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
                        "Shows on Sony Aath: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Shows on Sony Aath: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Shows on Sony Aath: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsBbcEarth(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsTvfPlay("tvfplay")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                isTVShowsPlayable = tvShowsModel.playable
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows on Sony BBC Earth")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Shows on Sony BBC Earth: " + tvShowsModel.message,
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
                        "Shows on Sony BBC Earth: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Shows on Sony BBC Earth: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Shows on Sony BBC Earth: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getTVShowsTvfPlay(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
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
                                tvShowsCategory = category
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    isTVShowsPlayable = tvShowsModel.playable
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "TVF Play")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "TVF Play: " + tvShowsModel.message,
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
                        "TVF Play: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "TVF Play: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "TVF Play: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getTVShowsEpicon(category: String) {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getTVShows(category, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        getTVShowsSonyMarathi("sonymarathi")
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val tvShowsModel: TVShowsResModel =
                                gson.fromJson(reader, TVShowsResModel::class.java)
                            if (tvShowsModel.status == 1) {
                                tvShowsCategory = category
                                val moviePresenter = activity?.let {
                                    TVShowsPresenter(
                                        it,
                                        category
                                    )
                                }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (tvShowsDatumModel in tvShowsModel.data!!) {
                                    arrayObjectAdapter.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    isTVShowsPlayable = tvShowsModel.playable
                                    if (tvShowsModel.data.size > 9 || tvShowsModel.data.size == 9) {
                                        arrayObjectAdapter.add(
                                            TVShowsDatumModel(
                                                "-1",
                                                showCategory = category
                                            )
                                        )
                                    }
                                    val headerItem = HeaderItem(0, "Shows on Epicon")
                                    mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Epicon: " + tvShowsModel.message,
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
                        "Epicon: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        activity,
                        "Epicon: " + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "Epicon: " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    companion object {
        var mRowsAdapter: ArrayObjectAdapter? = null
    }
}