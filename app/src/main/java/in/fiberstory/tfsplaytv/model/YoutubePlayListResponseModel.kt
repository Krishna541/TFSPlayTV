package `in`.fiberstory.tfsplaytv.model

data class YoutubePlayListResponseModel(
    val etag: String? = null,
    val items: List<Item>? = null,
    val kind: String? = null,
    val nextPageToken: String? = null,
    val pageInfo: PageInfo? = null
)