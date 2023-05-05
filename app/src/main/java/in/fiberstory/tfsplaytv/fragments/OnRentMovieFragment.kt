package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.activities.CommanActivity
import `in`.fiberstory.tfsplaytv.activities.EpisodeActivity
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import `in`.fiberstory.tfsplaytv.activities.PlexigoOnContentDetailActivity
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.*
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.network.PlexigoAPIClient
import `in`.fiberstory.tfsplaytv.presenter.CustomListRowPresenter
import `in`.fiberstory.tfsplaytv.presenter.OnRentmoviePresenter
import `in`.fiberstory.tfsplaytv.presenter.TVShowsPresenter
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class OnRentMovieFragment : BrowseSupportFragment() {
    private lateinit var navigationMenuCallback: NavigationMenuCallback
    private var customListRowPresenter: CustomListRowPresenter? = null
    private var tvOnRentCategory: String = ""
    lateinit var  onRentModel : PlexigoOnRentApiChanneResponse
    var userId:kotlin.Int = 0
    lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        handler.postDelayed({ startEntranceTransition() }, 500)
        userId = sharedPreferences.getInt("PlexigoUserId", 0)
        setUpAdapter()
        allOnRentMovies
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
                val indexOfItem = mRowsAdapter!!.indexOf(row)
                Log.d("TAG", "initListeners: "+ mRowsAdapter!!.indexOf(row))
                var selectedMovieItem = item as Content
                if (selectedMovieItem.id == "-1"
                ) {
                   // Log.d(TAG, "initListeners: "+onRentModel.data.categories[i])
                    activity?.startActivity(
                        CommanActivity.createOnRentIntent(
                            activity,
                            selectedMovieItem.showCategory,
                            101,
                            onRentModel.data.categories.get(indexOfItem).channelID,
                            userId
                        )
                    )
                } else {

                    activity?.startActivity(
                        PlexigoOnContentDetailActivity.createIntentMovie(
                            context, selectedMovieItem.contentId.toString(),0
                        )
                    )
                }
            }
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    private val allOnRentMovies: Unit
        get() {
            var index: Int
            for (m in 0 until mRowsAdapter!!.size()) {
                index = m + 1
                mRowsAdapter!!.removeItems(index, mRowsAdapter!!.size())
            }
            getOnRentMovie("onRentMovie")
        }

    private fun getOnRentMovie(category: String) {
        val apiInterface =   PlexigoAPIClient.getClient("", APIInterface.DOMAIN_URL)?.create(APIInterface::class.java)
        val req = PlexigoOnRentReqModel(userId)
        val call: Call<String> = apiInterface!!.getOnRentChannelCategoryListing(req)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            onRentModel =
                                gson.fromJson(reader, PlexigoOnRentApiChanneResponse::class.java)
                            if (onRentModel.status == "success") {
                               // MainActivity.isTVShowsPlayable = tvShowsModel.playable
                                tvOnRentCategory = category
                                val moviePresenter = activity?.let {
                                    OnRentmoviePresenter(
                                        it,
                                        category
                                    )
                                }


                                for (item in 0 until onRentModel.data.categories.size) {
                                    val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                    for(i in 0 until onRentModel.data.categories.get(item).content.size){
                                        arrayObjectAdapter.add(onRentModel.data.categories.get(item).content.get(i))
                                    }
                                    if(arrayObjectAdapter.size() > 0){
                                        if(onRentModel.data.categories.get(item).content.size > 10 || onRentModel.data.categories.get(item).content.size == 10 ){
                                            arrayObjectAdapter.add(Content("-1", showCategory = category , userId))
                                        }
                                        val headerItem = HeaderItem(0, onRentModel.data.categories.get(item).channelName)
                                        mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
                                        Log.d("item", "dapret: "+ mRowsAdapter)
                                    }

                                }
//                                if (arrayObjectAdapter.size() > 0) {
////                                    for(i in onRentModel.data.categories!!){
////                                        val headerItem = HeaderItem(0, i.channelName)
////                                        mRowsAdapter!!.add(ListRow(headerItem, arrayObjectAdapter))
////                                        Log.d("TAG", "onResponse: count")
////                                    }
//                                    //arrayObjectAdapter.add(TVShowsDatumModel("-1", showCategory = category))
//
//                                } else {
//                                    Toast.makeText(
//                                        activity,
//                                        "You have not availed any tv shows yet",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Originals: " + onRentModel.data.message,
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


    companion object {
        var mRowsAdapter: ArrayObjectAdapter? = null
    }
}