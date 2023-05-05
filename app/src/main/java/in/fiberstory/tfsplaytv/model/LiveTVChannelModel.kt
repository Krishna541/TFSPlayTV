package `in`.fiberstory.tfsplaytv.model


import com.google.gson.annotations.SerializedName

data class LiveTVItemModel(
    @SerializedName("channel_name")
    val channelName: String? = "",
    @SerializedName("deeplink_url_mobile")
    val deeplinkUrlMobile: String? = "",
    @SerializedName("is_leanback")
    val isLeanback: String? = "",
    @SerializedName("service_logo_128")
    val serviceLogo128: String? = "",
    @SerializedName("deeplink")
    val deeplink: String? = "",
    @SerializedName("logo_landscape")
    val logoLandscape: String? = "",
    @SerializedName("androidtv_package_id")
    val apkPackageId: String? = "",
    @SerializedName("channel_info")
    val channelInfo: String? = "",
    @SerializedName("service_logo_64")
    val serviceLogo64: String? = "",
    @SerializedName("genre")
    val genre: String? = "",
    @SerializedName("ott_service_name")
    val ottServiceName: String? = "",
    @SerializedName("deeplink_url_tv")
    val deeplinkUrlTv: String? = "",
    @SerializedName("free")
    val free: String? = "",
    @SerializedName("category_rank")
    val categoryRank: String? = "",
    @SerializedName("channel_id")
    val channelId: String? = "",
    @SerializedName("apple_package_id")
    val applePackageId: String? = "",
    @SerializedName("headerTitle")
    var headerTitle: String? = "",
    @SerializedName("channelSection")
    var channelSection: ArrayList<LiveTVItemModel>? = null
)


data class LiveTVResModel(
    @SerializedName("Data")
    val data: List<LiveTVItemModel>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("playable")
    val playable: Int = 0
)


