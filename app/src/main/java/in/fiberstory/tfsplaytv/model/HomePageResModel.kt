package `in`.fiberstory.tfsplaytv.model


import com.google.gson.annotations.SerializedName

data class DataItem(
    @SerializedName("data") val data: ArrayList<HomeModel>?,
    @SerializedName("title") val title: String = "",
    @SerializedName("type") val type: String = ""
)


data class HomePageListingModel(
    @SerializedName("Data") val data: ArrayList<DataItem>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int? = null,
    @SerializedName("playable") val playable: Int? = null,
    @SerializedName("featured_content_id") val featured_content_id:String = "",
    @SerializedName("featured_content_image") val featured_content_image: String = "",
    @SerializedName("youtube_playlist") val youtube_playlist: ArrayList<String>

)

data class HomeModel(

    @SerializedName("isMore") var isMore: String? = null,
    @SerializedName("channel_id") var channelId: String? = null,
    @SerializedName("show_id") var show_id: String? = null,
    @SerializedName("show_name") var show_name: String? = null,
    @SerializedName("channel_name") var channelName: String? = null,
    @SerializedName("free") var free: String? = null,
    @SerializedName("poster_tv") var posterTv: String? = null,
    @SerializedName("channel_info") var channelInfo: String? = null,
    @SerializedName("deeplink") var deeplink: String? = null,
    @SerializedName("deeplink_url_mobile") var deeplinkUrlMobile: String? = null,
    @SerializedName("deeplink_url_tv") var deeplinkUrlTv: String? = null,
    @SerializedName("androidtv_package_id") var apkPackageId: String? = null,
    @SerializedName("apple_package_id") var applePackageId: String? = null,
    @SerializedName("ott_service_name") var ottServiceName: String? = null,
    @SerializedName("service_logo_128") var serviceLogo128: String? = null,
    @SerializedName("service_logo_64") var serviceLogo64: String? = null,
    @SerializedName("home_category_id") var homeCategoryId: String? = null,
    @SerializedName("home_category_name") var homeCategoryName: String? = null,
    @SerializedName("content_type") var content_type: String? = null,
    @SerializedName("genre1") val genre1: String = "",
    @SerializedName("genre2") val genre2: String = "",
    @SerializedName("genre3") val genre3: String = "",
    @SerializedName("censor_rating") val censorRating: String = "",
    @SerializedName("duration") val duration: String = "",
    @SerializedName("movie_info") val movieInfo: String? = "",
    @SerializedName("movie_name") val movieName: String? = "",
    @SerializedName("cast_and_crew") val castAndCrew: String? = "",
    @SerializedName("release_year") val releaseYear: String? = "",
    @SerializedName("detail_page_cover_tv") val detailPageCoverTv: String? = "",
    @SerializedName("channel_icon") val channelIcon : String?= "",
    @SerializedName("channel_url") val channel_url : String?=""

    )




