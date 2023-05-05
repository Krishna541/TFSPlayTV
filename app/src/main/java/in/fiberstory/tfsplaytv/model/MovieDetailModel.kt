package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailModel(
    @SerializedName("contentid")
    var contentid: Int = 0,

    @SerializedName("contentname")
    var contentname: String? = null,

    @SerializedName("synopsis")
    var synopsis: String? = null,

    @SerializedName("ppv")
    var ppv: String? = null,

    @SerializedName("ppvTitle")
    var ppvTitle: String? = null,

    @SerializedName("inr")
    var inr: String? = null,

    @SerializedName("usd")
    var usd: String? = null,

    @SerializedName("ppvValidity")
    var ppvValidity: String? = null,

    @SerializedName("ppvType")
    var ppvType: String? = null,

    @SerializedName("genere")
    var genere: String? = null,

    @SerializedName("imagepath")
    var imagepath: String? = null,

    @SerializedName("trailerpath")
    var trailerpath: String? = null,

    @SerializedName("videopath")
    var videopath: String? = null,

    @SerializedName("releaseyear")
    var releaseyear: String? = null,

    @SerializedName("rating")
    var rating: String? = null,

    @SerializedName("censorrating")
    var censorrating: String? = null,

    @SerializedName("duration")
    var duration: String? = null,

    @SerializedName("isWatchLater")
    var isWatchLater: String? = null,

    @SerializedName("dashURL")
    var dashURL: String? = null,

    @SerializedName("widevineLicenseURL")
    var widevineLicenseURL: String? = null,

    @SerializedName("ottPlatforms")
    @Expose
     val all_ott_models: ArrayList<OTTModel>? = null,

    @SerializedName("castCrew")
    @Expose
     val all_casts: ArrayList<CastModel>? = null,

    @SerializedName("tacLink")
    var tacLink: String? = null,

    @SerializedName("tacText")
    var tacText: String? = null,

    @SerializedName("isFilmFestival")
    var isFilmFestival: String? = null,

    @SerializedName("isLive")
    var isLive: String? = null,

    @SerializedName("isQnAEnabled")
    var isQnAEnabled: String? = null,

    @SerializedName("channelID")
    var channelID: String? = null,

    @SerializedName("watchDuration")
    var watchDuration: String? = null,

    @SerializedName("pollInterval")
    var pollInterval: String? = null,

    @SerializedName("country")
    var country: String? = null,

//    @SerializedName("paymentGateway")
//    var paymentGateways: ArrayList<PaymentGatewayModel>? = null,

    @SerializedName("rentValidity")
    var rentValidity: String? = null,

//    @SerializedName("languages")
//    var languages: ArrayList<LanguageModel>? = null,

    @SerializedName("isDRMContent")
    var isDRMContent: String? = null,

    @SerializedName("contentType")
    var contentType: String? = null,

    @SerializedName("posterpath")
    var posterpath: String? = null,

    @SerializedName("userLikes")
    var userLikes : Int = 0,

    @SerializedName("showLikes")
    var showLikes: String? = null,


    @SerializedName("seasons")
    @Expose
    val seasons: ArrayList<TvSeriesSeason>? = null
)

data class TvSeriesSeason(
    @SerializedName("seasonId") @Expose
     var seasonId: Int? = null,
    @SerializedName("seasonname")
    @Expose
    val seasonname: String? = null,

    @SerializedName("airdate")
    @Expose
    val airdate: String? = null,

    @SerializedName("overview")
    @Expose
    val overview: String? = null,

    @SerializedName("episodeCount")
    @Expose
    val episodeCount: String? = null,

    @SerializedName("poster")
    @Expose
    val poster: String? = null,

    @SerializedName("episodes")
    @Expose
    val episodes: java.util.ArrayList<TvSeriesEpisode>? = null
)

data class TvSeriesEpisode(
    @SerializedName("episodeid") @Expose
     var episodeid: Int? = null,
        @SerializedName("episodenumber")
    @Expose
     val episodenumber: String? = null,

    @SerializedName("contentEpisodeID")
    @Expose
     val contentEpisodeID: String? = null,

    @SerializedName("episodename")
    @Expose
     val episodename: String? = null,

    @SerializedName("airdate")
    @Expose
     val airdate: String? = null,

    @SerializedName("overview")
    @Expose
     val overview: String? = null,

    @SerializedName("duration")
    @Expose
     val duration: String? = null,

    @SerializedName("poster")
    @Expose
     val poster: String? = null,

    @SerializedName("watchDuration")
    @Expose
     val watchDuration: String? = null,

    @SerializedName("videopath")
    @Expose
     val videopath: String? = null

)

data class OTTModel(
    @SerializedName("ottPlatformId")
    @Expose
     var ottPlatformId: Int? = null,

    @SerializedName("ottPlatform")
    @Expose
     val ottPlatform: String? = null,

    @SerializedName("url")
    @Expose
     val url: String? = null,

    @SerializedName("imagePath")
    @Expose
     val imagePath: String? = null,

    @SerializedName("wideImagePath")
    @Expose
     val wideImagePath: String? = null,

    @SerializedName("buttonText")
    @Expose
     val buttonText: String? = null,

    @SerializedName("buttonIcon")
    @Expose
     val buttonIcon: String? = null
)

data class CastModel(
    @SerializedName("castCrewId")
    @Expose
     var castCrewId: Int? = null,
    @SerializedName("name")
    @Expose
     val name: String? = null,

    @SerializedName("bio")
    @Expose
     val bio: String? = null,

    @SerializedName("imagepath")
    @Expose
     val imagepath: String? = null
)
