package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.SerializedName

data class Item(
    val etag: String? = "",
    val id: String? = "",
    val kind: String? = "",
    val snippet: Snippet?= null ,
    @SerializedName("isMore")
    val isMore: String? = "",
    @SerializedName("content_type")
    var content_type: String? = null,
    @SerializedName("channelId")
    var channelId : String?= null
)