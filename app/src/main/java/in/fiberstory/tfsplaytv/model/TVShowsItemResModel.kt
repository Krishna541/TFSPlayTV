package `in`.fiberstory.tfsplaytv.model


import com.google.gson.annotations.SerializedName

data class TVShowsDatumModel(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("language_code")
    val languageCode: String = "",
    @SerializedName("poster_tv")
    val posterTv: String = "",
    @SerializedName("service_logo_64")
    val serviceLogo64: String = "",
    @SerializedName("show_id")
    val showId: String = "",
    @SerializedName("service_logo_128")
    val serviceLogo128: String = "",
    @SerializedName("show_name")
    val showName: String = "",
    @SerializedName("release_year")
    val releaseYear: String = "",
    @SerializedName("poster_mobile")
    val posterMobile: String = "",
    @SerializedName("free")
    val free: String = "",
    @SerializedName("showCategory")
    val showCategory: String? = "",

)


data class TVShowsResModel(
    @SerializedName("next_page")
    val nextPage: Int = 0,
    @SerializedName("Data")
    val data: List<TVShowsDatumModel>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("playable")
    val playable: Int = 0
)


