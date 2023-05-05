package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.isEpisodePlayable
import `in`.fiberstory.tfsplaytv.model.EpisodeDatumModel
import `in`.fiberstory.tfsplaytv.model.EpisodeModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.pagination.PaginationAdapter
import `in`.fiberstory.tfsplaytv.pagination.PostAdapter
import `in`.fiberstory.tfsplaytv.presenter.CustomListRowPresenter
import `in`.fiberstory.tfsplaytv.presenter.EpisodePresenter
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
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

class EpisodeFragment : BrowseSupportFragment() {
    private var apiInterface: APIInterface? = null
    private var responseFailed = false
    private var mPostAdapter: PostAdapter? = null
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var mCallback: OnBrowseRowListener? = null
    private var bundle: Bundle? = null
    private var mShowsID: String? = null
    private var mShowName: String? = null

    interface OnBrowseRowListener {
        fun onItemSelected(item: Any?, index: Long)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        upFocus()
        val selector = CustomListRowPresenter(activity)
        mRowsAdapter = ArrayObjectAdapter(selector)
        mPostAdapter = PostAdapter(activity, activity?.let { EpisodePresenter(it, "tag") }, "tag")
        bundle = activity?.intent?.extras
        if (bundle != null) {
            mShowsID = bundle!!.getString("TVSHOWS")
            mShowName = bundle!!.getString("TVSHOWS_NAME")
            getLatestEpisode(mPostAdapter, mShowsID)
            adapter = mRowsAdapter
        }
        onItemViewSelectedListener =
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                mCallback!!.onItemSelected(item, row.headerItem.id)
                if (item is EpisodeDatumModel) {
                    val index = mRowsAdapter!!.indexOf(row)
                    val adapter: PostAdapter =
                        (mRowsAdapter!![index] as ListRow).adapter as PostAdapter

                    itemViewHolder?.view?.setOnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_DOWN) {
                            when (keyCode) {
                                KeyEvent.KEYCODE_DPAD_DOWN -> {
                                    return@setOnKeyListener true
                                }
                            }
                        }
                        false
                    }
                    if (adapter.get(adapter.size() - 1) == item && adapter.shouldLoadNextPage()) {
                        getLatestEpisode(adapter, mShowsID)
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isHeadersTransitionOnBackEnabled = true
        headersState = HEADERS_DISABLED
        mCallback = try {
            activity as OnBrowseRowListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnBrowseRowListener: " + e)
        }
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        val handler = Handler()
        handler.postDelayed({ startEntranceTransition() }, 500)
    }

    private fun getLatestEpisode(postAdapter: PostAdapter?, showID: String?) {
        if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
        val options: Map<String, String?>? = postAdapter?.adapterOptions
        val tag = options?.get(PaginationAdapter.KEY_TAG)
        val anchor = options?.get(PaginationAdapter.KEY_ANCHOR)
        val nextPage = options?.get(PaginationAdapter.KEY_NEXT_PAGE)
        apiInterface = APIClient.getClient().create<APIInterface>(APIInterface::class.java)
        val call: Call<String>? = apiInterface?.getEpisode(showID, nextPage, activity?.let {
            UserPreferences.getUserData(
                it
            ).subscriberId})
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val episodeModel: EpisodeModel =
                                gson.fromJson<EpisodeModel>(reader, EpisodeModel::class.java)
                            if (episodeModel.status == 1) {


                                isEpisodePlayable = episodeModel.playable
                                postAdapter?.removeLoadingIndicator()

                                val header = HeaderItem(0, "All Episodes")
                                mRowsAdapter!!.add(ListRow(header, mPostAdapter))

                                if (anchor == null) {
                                    if (anchor != null) {
                                        postAdapter.setAnchor(anchor)
                                    }
                                }
                                postAdapter?.setNextPage(
                                    episodeModel.nextPage.toString().toInt()
                                )
                                postAdapter?.addAllEpisodeItems(episodeModel.data)
                            }else{
                                val header = HeaderItem(0, "")
                                mRowsAdapter!!.add(ListRow(header, mPostAdapter))
                                mCallback?.onItemSelected(episodeModel,1)
                                postAdapter?.removeLoadingIndicator()
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
                mPostAdapter?.removeLoadingIndicator()
                if (mPostAdapter?.size() == 0) {
                    mPostAdapter?.showTryAgainCard()
                } else {
                    Toast.makeText(
                        activity, "Sorry, there was an error loading more data", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun upFocus() {
        if (view != null) {
            val viewToFocusUp =
                activity?.findViewById<View>(`in`.fiberstory.tfsplaytv.R.id.btnWatchNow)
            viewToFocusUp?.requestFocus()
            val browseFrameLayout =
                view?.findViewById<BrowseFrameLayout>(androidx.leanback.R.id.browse_frame)
            browseFrameLayout?.onFocusSearchListener = OnFocusSearchListener { focused, direction ->
                if (direction == View.FOCUS_UP) {
                    viewToFocusUp
                } else {
                    null
                }
            }
        }
    }
}