/*
 * Copyright (c) 2014 The Android Open Source Project
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
package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.*
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isLiveTVPlayable
import `in`.fiberstory.tfsplaytv.activities.SearchActivity.Companion.searchNotFound
import `in`.fiberstory.tfsplaytv.activities.SearchActivity.Companion.txtErrorMessage
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.model.LiveTVItemModel
import `in`.fiberstory.tfsplaytv.model.SearchModel
import `in`.fiberstory.tfsplaytv.model.TVShowsDatumModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.presenter.CustomListRowPresenter
import `in`.fiberstory.tfsplaytv.presenter.DefaultCardPresenterSelector
import `in`.fiberstory.tfsplaytv.presenter.MoviePresenter
import `in`.fiberstory.tfsplaytv.presenter.TVShowsPresenter
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.userLoginDialogAlert
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.leanback.widget.BrowseFrameLayout.OnFocusSearchListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import javax.net.ssl.SSLHandshakeException

class SearchFragment : BrowseSupportFragment() {
  //  private lateinit var navigationMenuCallback: NavigationMenuCallback

    private var customListRowPresenter: CustomListRowPresenter? = null
    private var mContext: SearchActivity? = null
    var movieAdded = false
    var showsAdded = false
    var channelAdded = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        upFocus()
        initListeners()
    }

        fun upFocus() {
        if (view != null) {
            val viewToFocusUp = activity?.findViewById<View>(R.id.rgKeyboardType)
            viewToFocusUp?.requestFocus()
            val browseFrameLayout: BrowseFrameLayout? =
                view?.findViewById(androidx.leanback.R.id.browse_frame)
            browseFrameLayout?.onFocusSearchListener =
                OnFocusSearchListener { _, direction ->
                    if (direction == View.FOCUS_UP) {
                        viewToFocusUp
                    } else {
                        null
                    }
                }
        }
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
        initListerner()
        customListRowPresenter = CustomListRowPresenter(activity)
        mRowsAdapter = ArrayObjectAdapter(customListRowPresenter)
        adapter = mRowsAdapter
        mContext = activity as SearchActivity?
        row
    }

    private fun initListerner() {
        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                Log.e("view all", "clicked")
                if (item.toString().contains("isFromMovie")) {
                    var selectedItem = item as DocumentaryItemsModel
                    if (selectedItem.isMore == "-1") {
                        activity?.startActivity(
                            CommanActivity.createIntentMovie(
                                activity, selectedItem.movieCategory, 0
                            )
                        )
                    } else {
                        activity?.startActivity(
                            DetailsActivity.createIntentMovie(
                                activity, selectedItem
                            )
                        )
                    }
                } else {
                    var selectedItem = item as TVShowsDatumModel
                    if (selectedItem.id == "-1") {
                        activity?.startActivity(
                            CommanActivity.createIntentMovie(
                                activity, selectedItem.showCategory, 1
                            )
                        )
                    } else {
                        activity?.startActivity(
                            EpisodeActivity.createIntentShow(
                                activity, selectedItem.showId, selectedItem.showName
                            )
                        )
                    }
                }

            }
    }

    private val row: Unit
        get() {
            var index: Int
            for (m in 0 until (mRowsAdapter?.size()!!)) {
                index = m + 1
                mRowsAdapter?.removeItems(index, mRowsAdapter!!.size())
            }
        }

    fun getSearchResult(query: String?) {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val call: Call<String> = apiInterface.getSearch(query)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                mContext?.mLoading?.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    try {
                        Log.d("SEARCH_MAYANK:", "onResponse: " + response.body().toString())
                        val gson = Gson()
                        val reader: Reader = StringReader(response.body())
                        val searchModel: SearchModel =
                            gson.fromJson(reader, SearchModel::class.java)
                        val parentObjAdapter =
                            ArrayObjectAdapter(CustomListRowPresenter(activity))
                        if (searchModel.status == 1) {
                            searchNotFound?.visibility = View.GONE
                            if (searchModel.movies != null) {
                                val moviePresenter = activity?.let { MoviePresenter(it, "all") }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in searchModel.movies!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    val headerItem = HeaderItem("All Movies")
                                    parentObjAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                    mRowsAdapter?.setItems(
                                        parentObjAdapter.unmodifiableList<Any>(),
                                        null
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            if (searchModel.documentaries != null) {
                                val moviePresenter =
                                    activity?.let { MoviePresenter(it, "all") }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in searchModel.documentaries!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    val headerItem = HeaderItem("All Documentaries")
                                    parentObjAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                    mRowsAdapter?.setItems(
                                        parentObjAdapter.unmodifiableList<Any>(),
                                        null
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            if (searchModel.shortfilms != null) {
                                val moviePresenter = activity?.let { MoviePresenter(it, "all") }
                                val arrayObjectAdapter = ArrayObjectAdapter(moviePresenter)
                                for (movieDatumModel in searchModel.shortfilms!!) {
                                    arrayObjectAdapter.add(movieDatumModel)
                                }
                                if (arrayObjectAdapter.size() > 0) {
                                    val headerItem = HeaderItem("All Short Films")
                                    parentObjAdapter.add(ListRow(headerItem, arrayObjectAdapter))
                                    mRowsAdapter?.setItems(
                                        parentObjAdapter.unmodifiableList<Any>(),
                                        null
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any movies yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            if (searchModel.shows != null) {
                                val tvShowsPresenter = activity?.let { TVShowsPresenter(it, "all") }
                                val arrayObjectAdapter1 = ArrayObjectAdapter(tvShowsPresenter)
                                for (tvShowsDatumModel in searchModel.shows!!) {
                                    arrayObjectAdapter1.add(tvShowsDatumModel)
                                }
                                if (arrayObjectAdapter1.size() > 0) {
                                    val headerItem = HeaderItem("All TV Shows")
                                    parentObjAdapter.add(ListRow(headerItem, arrayObjectAdapter1))
                                    mRowsAdapter?.setItems(
                                        parentObjAdapter.unmodifiableList<Any>(),
                                        null
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any tv shows yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            if (searchModel.channels != null) {
//                                val channelPresenter = ChannelPresenter(activity as MainActivity?)
                                val presenterSelector = activity?.baseContext?.let {
                                    DefaultCardPresenterSelector(it, "LiveTV")
                                }

                                val arrayObjectAdapter2 = ArrayObjectAdapter(presenterSelector)
                                for (channelDatumModel in searchModel.channels!!) {
                                    arrayObjectAdapter2.add(channelDatumModel)
                                }
                                if (arrayObjectAdapter2.size() > 0) {
                                    val headerItem = HeaderItem("Live Channels")
                                    parentObjAdapter.add(ListRow(headerItem, arrayObjectAdapter2))
                                    mRowsAdapter?.setItems(
                                        parentObjAdapter.unmodifiableList<Any>(),
                                        null
                                    )
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "You have not availed any channels yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            searchNotFound?.visibility = View.VISIBLE
                            txtErrorMessage?.text = getString(
                                R.string.no_search_results,
                                query
                            )
                            mRowsAdapter?.setItems(parentObjAdapter.unmodifiableList<Any>(), null)
                            flagPosition=true
                        }
                    } catch (exception: IllegalStateException) {
                    } catch (exception: JsonSyntaxException) {
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    refreshUI()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                mContext?.mLoading?.visibility = View.INVISIBLE
                if (t.cause != null && t.cause is SSLHandshakeException) {
                    Toast.makeText(mContext, "SSL Handshake Exception", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(mContext, "Unable to get response from server", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun refreshUI() {
        customListRowPresenter = CustomListRowPresenter(activity)
        mRowsAdapter = ArrayObjectAdapter(customListRowPresenter)
        adapter = mRowsAdapter
        row
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = SearchFragment.mRowsAdapter?.indexOf(row)

                val indexOfItem =
                    ((row as ListRow).adapter as ArrayObjectAdapter).indexOf(item)

                itemViewHolder?.view?.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            /*
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                if (indexOfItem == 0) {
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            }
                            */

                            KeyEvent.KEYCODE_BACK -> {

                                if(indexOfItem != 0){
                                    flagPosition = false
                                    rowsSupportFragment.setSelectedPosition(indexOfRow!!, true, object : ListRowPresenter.SelectItemViewHolderTask(0) {
                                        override fun run(holder: Presenter.ViewHolder?) {
                                            super.run(holder)
                                            holder?.view?.postDelayed({
                                                holder.view.requestFocus()
                                            }, 10)
                                        }
                                    })
                                }
                                else{
                                    flagPosition = true
                                }

                                //navigationMenuCallback.navMenuToggle(true)

                            }
/*

                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (indexOfRow != null) {
                                    var count = TVShowsFragment.mRowsAdapter!!.size()-1
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
*/

                        }
                    }
                    false
                }
            }

        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                Log.e("view all", "clicked")


                if (item.toString().contains("isFromMovie")) {
                    var selectedItem = item as DocumentaryItemsModel
                    if (selectedItem.isMore == "-1") {
                        activity?.startActivity(
                            CommanActivity.createIntentMovie(
                                activity, selectedItem.movieCategory, 0
                            )
                        )
                    } else {
                        activity?.startActivity(
                            DetailsActivity.createIntentMovie(
                                activity, selectedItem
                            )
                        )
                    }
                }
                else if(item.toString().contains("channelSection")) {
                    var liveItem = item as LiveTVItemModel
                    if (CheckUserLogin.loadPrefs(activity) != 1) {
                        activity?.userLoginDialogAlert(requireContext())

                    } else {
                        if (MainActivity.appInstalledOrNot(liveItem.apkPackageId)
                        ) {
                            if (isLiveTVPlayable == 1) {

                                MainActivity.launchAppUsingSSO(
                                    liveItem.deeplinkUrlTv,
                                    liveItem.ottServiceName,
                                    liveItem.apkPackageId
                                )

                            } else {
                                MainActivity.noActiveSubscription(
                                    liveItem.ottServiceName
                                )
                            }
                        } else {
                            appNotFound(liveItem.ottServiceName)
                        }
                    }
                }

                else {
                    var selectedItem = item as TVShowsDatumModel
                    if (selectedItem.id == "-1") {
                        activity?.startActivity(
                            CommanActivity.createIntentMovie(
                                activity, selectedItem.showCategory, 1
                            )
                        )
                    } else {
                        activity?.startActivity(
                            EpisodeActivity.createIntentShow(
                                activity, selectedItem.showId, selectedItem.showName
                            )
                        )
                    }
                }
            }
    }

    fun appNotFound(serviceName: String?) {
        val dialog = Dialog(requireContext(), R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(MainActivity.context.resources.getColor(R.color.colorTransparent)))
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
            dialog.dismiss()
//                val intent = Intent(context, MainActivity::class.java)
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity)
//                    .toBundle()
//                context.startActivity(intent, bundle)
//                (context as Activity).finish()
        }
    }

    companion object {
        var mRowsAdapter: ArrayObjectAdapter? = null
        var flagPosition : Boolean = false
    }
}