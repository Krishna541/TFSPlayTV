package `in`.fiberstory.tfsplaytv.model

data class Snippet(
    val channelId: String? = null,
    val channelTitle: String? = null,
    val description: String? = null,
    val playlistId: String? = null,
    val position: Int? = null,
    val publishedAt: String? = null,
    val resourceId: ResourceId? = null,
    val thumbnails: Thumbnails? = null,
    val title: String? = null,
    val videoOwnerChannelId: String? = null,
    val videoOwnerChannelTitle: String? = null
)