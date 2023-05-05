package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.SerializedName

data class PlexigoOnRentApiChanneResponse(
    val `data`: PlexigoOnRentData,
    val status: String,
    val statusCode: String
)

data class PlexigoOnRentData(
    val categories: List<Category>,
    val message: Any
)

data class Category(
    val channelID: Int,
    val channelName: String,
    val content: List<Content>,
    val pageCount: Int
)

data class Content(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("showCategory")
    val showCategory: String? = "",
    var userId : Int ? = null,
    val contentId: Int? = null,
    val contentName: String? = "",
    val externalWebLink: String? = "",
    val imagePath: String = "",
    val isPlaylist: Int?= null,
    val isWatchLater: String?="",
    val language: String? = "",
    val ottWideImagePath: String? = "",
    val synopsis: String? = "",
    val tag: String?="",
    val userLikes: Int? = null,
    val wideImagePath: String ? = "",
    val year: Int?= null
)