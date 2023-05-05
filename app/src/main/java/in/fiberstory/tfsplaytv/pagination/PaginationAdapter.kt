package `in`.fiberstory.tfsplaytv.pagination

import `in`.fiberstory.tfsplaytv.R
import android.content.Context
import android.os.Handler
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

abstract class PaginationAdapter(
    private val mContext: Context,
    private val mPresenter: Presenter,
    tag: String
) : ArrayObjectAdapter() {
    private var mNextPage: Int
    private val mLoadingPresenter: LoadingPresenter = LoadingPresenter()
    private val mLoadingPortraitPresenter: LoadingPortraitPresenter = LoadingPortraitPresenter()
    private val mIconItemPresenter: IconItemPresenter = IconItemPresenter()
    private var mRowTag: String
    private var mAnchor: String? = null
    private var mLoadingIndicatorPosition: Int
    fun setTag(tag: String) {
        mRowTag = tag
    }

    fun setNextPage(page: Int) {
        mNextPage = page
    }

    private fun setPresenterSelector() {
        presenterSelector = object : PresenterSelector() {
            override fun getPresenter(item: Any): Presenter {
                if (item is LoadingPortraitCardView) {
                    return mLoadingPortraitPresenter
                } else if (item is LoadingCardView) {
                    return mLoadingPresenter
                } else if (item is Option) {
                    return mIconItemPresenter
                }
                return mPresenter
            }
        }
    }

    val items: List<Any>
        get() = unmodifiableList()

    fun shouldShowLoadingIndicator(): Boolean {
        return mLoadingIndicatorPosition == -1
    }

    fun showLoadingIndicator() {
        Handler().post {
            mLoadingIndicatorPosition = size()
            if (mRowTag.equals("Short Films", ignoreCase = true)) {
                add(mLoadingIndicatorPosition, LoadingPortraitCardView(mContext))
            } else {
                add(mLoadingIndicatorPosition, LoadingCardView(mContext))
            }
            notifyItemRangeInserted(mLoadingIndicatorPosition, 1)
        }
    }

    fun removeLoadingIndicator() {
        removeItems(mLoadingIndicatorPosition, 1)
        notifyItemRangeRemoved(mLoadingIndicatorPosition, 1)
        mLoadingIndicatorPosition = -1
    }

    fun setAnchor(anchor: String) {
        mAnchor = anchor
    }

    fun addPosts(posts: List<*>) {
        if (posts.isNotEmpty()) {
            addAll(size(), posts)
        } else {
            mNextPage = 0
        }
    }

    fun shouldLoadNextPage(): Boolean {
        return shouldShowLoadingIndicator() && mNextPage != 0
    }

    val adapterOptions: Map<String, String?>
        get() {
            val map: MutableMap<String, String?> = HashMap()
            map[KEY_TAG] = mRowTag
            map[KEY_ANCHOR] = mAnchor
            map[KEY_NEXT_PAGE] = mNextPage.toString()
            return map
        }

    fun showReloadCard() {
        val option = Option(
            mContext.getString(R.string.title_no_videos),
            mContext.getString(R.string.message_check_again),
            R.drawable.ic_refresh_white
        )
        add(option)
    }

    fun showTryAgainCard() {
        val option = Option(
            mContext.getString(R.string.title_oops),
            mContext.getString(R.string.message_try_again),
            R.drawable.ic_refresh_white
        )
        add(option)
    }

    fun removeReloadCard() {
        if (isRefreshCardDisplayed) {
            removeItems(0, 1)
            notifyItemRangeRemoved(size(), 1)
        }
    }

    val isRefreshCardDisplayed: Boolean
        get() {
            val item = get(size() - 1)
            if (item is Option) {
                val option = item
                val noVideosTitle = mContext.getString(R.string.title_no_videos)
                val oopsTitle = mContext.getString(R.string.title_oops)
                return option.title == noVideosTitle || option.title == oopsTitle
            }
            return false
        }

    abstract fun addAllMovieItems(items: List<*>?)
    abstract val allMovieItems: List<*>?
    abstract fun addAllEpisodeItems(items: List<*>?)
    abstract fun addAllPlexigoExpisode(items: List<*>?)
    abstract fun addAllContentByChannel(items: List<*>?)
    abstract fun addAllYoutubePlaylistContent(items: List<*>?)

    abstract fun getAllTVShowsItems(): List<*>?
    abstract fun getAllEpisodeItems(): List<*>?
    abstract fun addAllTVShowsItems(items: List<*>?)
    abstract fun getAllPlexigoItems() : List<*>?
    abstract fun getAllContentByChannel() : List<*>?


    abstract fun getAllYoutubePlaylistContent() : List<*>?

    companion object {
        const val KEY_TAG = "tag"
        const val KEY_ANCHOR = "anchor"
        const val KEY_NEXT_PAGE = "next_page"
    }

    init {
        mLoadingIndicatorPosition = -1
        mNextPage = 1
        mRowTag = tag
        setPresenterSelector()
    }
}