package `in`.fiberstory.tfsplaytv.model


import com.google.gson.annotations.SerializedName

data class PromotionsItem(
    @SerializedName("icon_url") val iconUrl: String = "",
    @SerializedName("promotion_name") val promotionName: String = "",
    @SerializedName("enable") val enable: String = "",
    @SerializedName("provider_id") val providerId: String = "",
    @SerializedName("promotion_id") val promotionId: String = "",
    @SerializedName("provider_name") val providerName: String = "",
    @SerializedName("url") val url: String = "",
    @SerializedName("androidtv_package_id") val apk_package_id: String = "",
    @SerializedName("tvos_package_id") val tvos_package_id: String = "",
    @SerializedName("ott_service_name") val ott_service_name: String = "",
    @SerializedName("content_url_type") val content_url_type: String = "",
    @SerializedName("content_url") val content_url: String = "",
    @SerializedName("content_url_tvos") val content_url_tvos: String = "",
    @SerializedName("language_code") val language_code: String = "",
    @SerializedName("release_year") val release_year: String = "",
    @SerializedName("synopsis") val synopsis: String = "",
    @SerializedName("isFrom") val isFrom: String = "",
    @SerializedName("content_id") val content_id: String = "",
)


data class BannerModel(
    @SerializedName("promotions") val promotions: ArrayList<PromotionsItem>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)


