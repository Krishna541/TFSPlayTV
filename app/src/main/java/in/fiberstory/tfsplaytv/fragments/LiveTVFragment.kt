package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isLiveTVPlayable
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.LiveTVItemModel
import `in`.fiberstory.tfsplaytv.model.LiveTVResModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.presenter.DefaultCardPresenterSelector
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import `in`.fiberstory.tfsplaytv.utility.userLoginDialogAlert
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import androidx.leanback.widget.BrowseFrameLayout.OnFocusSearchListener
import com.androijo.tvnavigation.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import kotlin.properties.Delegates

@RequiresApi(Build.VERSION_CODES.O)
class LiveTVFragment : RowsSupportFragment() {

    private var mRowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(RowPresenterSelector())

    private lateinit var navigationMenuCallback: NavigationMenuCallback
    var rowCount by Delegates.notNull<Int>()


    init {
        callLiveChannelAPI()
        initAdapters()
        initListeners()
    }

    private fun callLiveChannelAPI() {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)

        val apiCall: Call<String> = apiInterface.getLiveTVChannelList(activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId
        })
        apiCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())

                            val liveTVModel: LiveTVResModel =
                                gson.fromJson(reader, LiveTVResModel::class.java)
                            if (liveTVModel.status == 1) {
                                isLiveTVPlayable = liveTVModel.playable
                                if (liveTVModel.data?.isNotEmpty() == true) {
                                    val selectedGenre = java.util.ArrayList<String?>()
                                    for (channelDatumModel in liveTVModel.data) {
                                        selectedGenre.add(channelDatumModel.genre)
                                    }
                                    val sectionHelper: ArrayList<LiveTVItemModel> =
                                        ArrayList()
                                    for (genre in removeDuplicates(selectedGenre)) {
                                        val dm = LiveTVItemModel()
                                        val singleItem: ArrayList<LiveTVItemModel> =
                                            ArrayList()
                                        for (channelDatumModel in liveTVModel.data) {
                                            if (channelDatumModel.genre == genre) {
                                                dm.headerTitle = genre
                                                singleItem.add(channelDatumModel)
                                                dm.channelSection = singleItem
                                            }
                                        }
                                        sectionHelper.add(dm)
                                    }
                                    if (sectionHelper.size > 0) {
                                        setLiveChannelsList(
                                            sectionHelper
                                        )
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "You have not availed any channels yet",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "" + response.message().toString(),
                            Toast.LENGTH_SHORT
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

    private fun setLiveChannelsList(sectionHelper: java.util.ArrayList<LiveTVItemModel>) {
        for (i in 0 until sectionHelper.size) {
            rowCount = sectionHelper.size

            mRowsAdapter.add(
                sectionHelper[i].channelSection?.let {
                    createHomeCardRow(
                        sectionHelper[i].headerTitle,
                        it
                    )
                }
            )
        }
    }

    private fun createHomeCardRow(
        headerTitle: String?,
        liveTVChannelList: java.util.ArrayList<LiveTVItemModel>
    ): Any {
        val presenterSelector = activity?.baseContext?.let {
            DefaultCardPresenterSelector(it, "LiveTV")
        }
        val adapter = ArrayObjectAdapter(presenterSelector)
        val headerItem = HeaderItem(headerTitle)
        for (data in liveTVChannelList) {
            adapter.add(data)
        }

        return BannerCardListRow(headerItem, adapter)
    }


    fun <T> removeDuplicates(list: java.util.ArrayList<T>): java.util.ArrayList<T> {
        val newList = java.util.ArrayList<T>()
        for (element in list) {
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        return newList
    }

    private fun initAdapters() {
        adapter = mRowsAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                val indexOfRow = mRowsAdapter.indexOf(row)

                val indexOfItem =
                    ((row as BannerCardListRow).adapter as ArrayObjectAdapter).indexOf(item)

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
                                    setSelectedPosition(indexOfRow, true, object : ListRowPresenter.SelectItemViewHolderTask(0) {
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
                            KeyEvent.KEYCODE_DPAD_DOWN ->{
                                if(indexOfRow >= rowCount-1) {
                                    setSelectedPosition(indexOfRow, true,
                                        object : ListRowPresenter.SelectItemViewHolderTask(
                                            0
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
                Log.e("view all", "clicked")

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
                        MainActivity.appNotFound(liveItem.ottServiceName)
                    }
                }

            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upFocus()
        selectFirstItem()
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    fun upFocus() {
        if (view != null) {
            val viewToFocusUp = activity?.findViewById<View>(R.id.horizontal_grid)
            viewToFocusUp?.requestFocus()
            val browseFrameLayout: BrowseFrameLayout? = view?.findViewById(androidx.leanback.R.id.browse_frame)
            browseFrameLayout?.onFocusSearchListener =
                OnFocusSearchListener { focused, direction ->
                    if (direction == View.FOCUS_UP) {
                        null
                    } else {
                        null
                    }
                }
        }
    }


    /**
     * this function can put focus or selected a specific item in a specific row
     */

    fun selectFirstItem() {
        setSelectedPosition(
            0,
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