package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.CommanActivity
import `in`.fiberstory.tfsplaytv.activities.DetailsActivity
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.DocumentariesResModel
import `in`.fiberstory.tfsplaytv.model.DocumentaryItemsModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.pagination.PaginationAdapter
import `in`.fiberstory.tfsplaytv.pagination.PostAdapter
import `in`.fiberstory.tfsplaytv.presenter.DocumentariesPresenter
import `in`.fiberstory.tfsplaytv.presenter.JustifiedVerticalGridPresenter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.BrowseFrameLayout
import androidx.leanback.widget.BrowseFrameLayout.OnFocusSearchListener
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.OnItemViewClickedListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class DocumentariesFragment : VerticalGridSupportFragment() {

    private lateinit var navigationMenuCallback: NavigationMenuCallback


    private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE
    private var responseFailed = false
    private var postAdapter: PostAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        setData()
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        val handler = Handler()
        handler.postDelayed({ startEntranceTransition() }, 500)
    }

    private fun setupFocusSearchListener() {
        val viewToFocusUp = activity?.findViewById<View>(R.id.horizontal_grid)
        viewToFocusUp?.requestFocus()
        val browseFrameLayout = view?.findViewById<View>(
            androidx.leanback.R.id.grid_frame
        ) as BrowseFrameLayout
        browseFrameLayout.requestFocus()

        browseFrameLayout.onFocusSearchListener = OnFocusSearchListener { focused, direction ->
            if (direction == View.FOCUS_UP) {
                viewToFocusUp
            } else {
                null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupFocusSearchListener()
    }

    private fun setData() {
        postAdapter =
            PostAdapter(activity, activity?.let { DocumentariesPresenter(it) }, "Documentaries")
        adapter = postAdapter
        documentaries
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->

            val itemIndex: Int = postAdapter!!.indexOf(item)

            itemViewHolder?.view?.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            if (itemIndex == 0) {
                                navigationMenuCallback.navMenuToggle(true)
                            }
                            else if(itemIndex % COLUMNS == 0){
                                navigationMenuCallback.navMenuToggle(true)
                            }
                        }

                        KeyEvent.KEYCODE_BACK -> {
                            if(itemIndex != 0){
                                setSelectedPosition(0)
                            }else{
                                navigationMenuCallback.navMenuToggle(true)
                            }
                        }
                    }
                }
                false
            }
            if (item is DocumentaryItemsModel) {
                val itemIndex: Int = postAdapter!!.indexOf(item)
                val minimumIndex = postAdapter!!.allMovieItems?.size?.minus(COLUMNS)
                if (itemIndex >= minimumIndex!! && postAdapter!!.shouldLoadNextPage()) {
                    documentaries
                }
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
                            "",
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
        var presenter = JustifiedVerticalGridPresenter(activity,ZOOM_FACTOR, false)
        presenter.numberOfColumns = COLUMNS
        presenter.shadowEnabled = false
        gridPresenter = presenter
        setSelectedPosition(0)
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    private val documentaries: Unit
        private get() {
            if (postAdapter?.shouldShowLoadingIndicator() == true) postAdapter?.showLoadingIndicator()
            val options: Map<String, String>? = postAdapter?.adapterOptions as Map<String, String>?
            val tag = options?.get(PaginationAdapter.KEY_TAG)
            val anchor = options?.get(PaginationAdapter.KEY_ANCHOR)
            val nextPage = options?.get(PaginationAdapter.KEY_NEXT_PAGE)
            val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
            val call: Call<String> = apiInterface.getDocumentary(nextPage)
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
                                        if (postAdapter?.size() == 0 && movieModel.data?.isEmpty() == true
                                        ) {
                                            postAdapter?.showReloadCard()
                                        } else {
                                            if (anchor == null) {
                                                if (anchor != null) {
                                                    postAdapter?.setAnchor(anchor)
                                                }
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

    companion object {
        private const val COLUMNS = 3
    }
}