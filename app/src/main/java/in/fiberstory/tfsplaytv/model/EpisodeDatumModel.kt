package `in`.fiberstory.tfsplaytv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel

class EpisodeDatumModel : Parcelable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("show_name")
    @Expose
    var showName: String? = null

    @SerializedName("season_no")
    @Expose
    var seasonNo: String? = null

    @SerializedName("episode_no")
    @Expose
    var episodeNo: String? = null

    @SerializedName("episode_title")
    @Expose
    var episodeTitle: String? = null

    @SerializedName("censor_rating")
    @Expose
    var censorRating: String? = null

    @SerializedName("episode_info")
    @Expose
    var episodeInfo: String? = null

    @SerializedName("episode_poster")
    @Expose
    var episodePoster: String? = null

    @SerializedName("poster_mobile")
    @Expose
    var posterMobile: String? = null

    @SerializedName("poster_tv")
    @Expose
    var posterTv: String? = null

    @SerializedName("duration")
    @Expose
    var duration: String? = null

    @SerializedName("deeplink")
    @Expose
    var deeplink: String? = null

    @SerializedName("androidtv_package_id")
    @Expose
    var apkPackageId: String? = null

    @SerializedName("ott_service_name")
    @Expose
    var ottServiceName: String? = null

    @SerializedName("is_leanback")
    @Expose
    var isLeanback: String? = null

    @SerializedName("deeplink_url_mobile")
    @Expose
    var deeplink_url_mobile: String? = null

    @SerializedName("deeplink_url_tv")
    @Expose
    var deeplink_url_tv: String? = null

    @SerializedName("service_logo_64")
    @Expose
    var service_logo_64: String? = null

    @SerializedName("service_logo_128")
    @Expose
    var service_logo_128: String? = null

    protected constructor(`in`: Parcel) {
        id = `in`.readValue(String::class.java.classLoader) as String?
        showName = `in`.readValue(String::class.java.classLoader) as String?
        seasonNo = `in`.readValue(String::class.java.classLoader) as String?
        episodeNo = `in`.readValue(String::class.java.classLoader) as String?
        episodeTitle = `in`.readValue(String::class.java.classLoader) as String?
        censorRating = `in`.readValue(String::class.java.classLoader) as String?
        episodeInfo = `in`.readValue(String::class.java.classLoader) as String?
        episodePoster = `in`.readValue(String::class.java.classLoader) as String?
        posterMobile = `in`.readValue(String::class.java.classLoader) as String?
        posterTv = `in`.readValue(String::class.java.classLoader) as String?
        duration = `in`.readValue(String::class.java.classLoader) as String?
        deeplink = `in`.readValue(String::class.java.classLoader) as String?
        apkPackageId = `in`.readValue(String::class.java.classLoader) as String?
        ottServiceName = `in`.readValue(String::class.java.classLoader) as String?
        isLeanback = `in`.readValue(String::class.java.classLoader) as String?
        deeplink_url_mobile = `in`.readValue(String::class.java.classLoader) as String?
        deeplink_url_tv = `in`.readValue(String::class.java.classLoader) as String?
        service_logo_64 = `in`.readValue(String::class.java.classLoader) as String?
        service_logo_128 = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(showName)
        dest.writeValue(seasonNo)
        dest.writeValue(episodeNo)
        dest.writeValue(episodeTitle)
        dest.writeValue(censorRating)
        dest.writeValue(episodeInfo)
        dest.writeValue(episodePoster)
        dest.writeValue(posterMobile)
        dest.writeValue(posterTv)
        dest.writeValue(duration)
        dest.writeValue(deeplink)
        dest.writeValue(apkPackageId)
        dest.writeValue(ottServiceName)
        dest.writeValue(isLeanback)
        dest.writeValue(deeplink_url_mobile)
        dest.writeValue(deeplink_url_tv)
        dest.writeValue(service_logo_64)
        dest.writeValue(service_logo_128)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<EpisodeDatumModel> {
        override fun createFromParcel(parcel: Parcel): EpisodeDatumModel {
            return EpisodeDatumModel(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeDatumModel?> {
            return arrayOfNulls(size)
        }
    }
}