package `in`.fiberstory.tfsplaytv.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DocumentaryItemsModel(
    @SerializedName("isMore")
    var isMore: String? = "",
    @SerializedName("poster_tv")
    val posterTv: String = "",
    @SerializedName("deeplink_url_mobile")
    val deeplinkUrlMobile: String = "",
    @SerializedName("is_leanback")
    val isLeanback: String = "",
    @SerializedName("genre1")
    val genre1: String? = "",
    @SerializedName("genre2")
    val genre2: String? = "",
    @SerializedName("poster_mobile")
    val posterMobile: String? = "",
    @SerializedName("movie_name")
    val movieName: String? = "",
    @SerializedName("androidtv_package_id")
    val apkPackageId: String? = "",
    @SerializedName("censor_rating")
    val censorRating: String? = "",
    @SerializedName("duration")
    val duration: String? = "",
    @SerializedName("language_code")
    val languageCode: String? = "",
    @SerializedName("genre3")
    val genre: String? = "",
    @SerializedName("ott_service_name")
    val ottServiceName: String? = "",
    @SerializedName("id")
    val movieId: String = "",
    @SerializedName("deeplink_url_tv")
    val deeplinkUrlTv: String? = "",
    @SerializedName("free")
    val free: String = "",
    @SerializedName("detail_page_cover_tv")
    val detailPageCoverTv: String? = "",
    @SerializedName("service_logo_128")
    val serviceLogo128: String = "",
    @SerializedName("deeplink")
    val deeplink: String = "",
    @SerializedName("release_year")
    val releaseYear: String? = "",
    @SerializedName("movie_info")
    val movieInfo: String? = "",
    @SerializedName("service_logo_64")
    val serviceLogo64: String = "",
    @SerializedName("detail_page_cover_mobile")
    val detailPageCoverMobile: String? = "",
    @SerializedName("cast_and_crew")
    val castAndCrew: String? = "",
    @SerializedName("apple_package_id")
    val applePackageId: String? = "",
    @SerializedName("movieCategory")
    val movieCategory: String? = "",
    @SerializedName("isFromMovie")
    val isFromMovie: String? = ""

) : Serializable


data class DocumentariesResModel(
    @SerializedName("next_page")
    val nextPage: Int = 0,
    @SerializedName("Data")
    val data: List<DocumentaryItemsModel>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("playable")
    val playable: Int = 0
)


