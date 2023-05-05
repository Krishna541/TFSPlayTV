package `in`.fiberstory.tfsplaytv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel

class EpisodeModel : Parcelable {
    @SerializedName("show_info")
    @Expose
    var show_info: String? = null

    @SerializedName("censor_rating")
    @Expose
    var censor_rating: String? = null

    @SerializedName("language_code")
    @Expose
    var language_code: String? = null

    @SerializedName("release_year")
    @Expose
    var release_year: String? = null

    @SerializedName("poster_tv")
    @Expose
    var poster_tv: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("Data")
    @Expose
    var data: List<EpisodeDatumModel?>? = null

    @SerializedName("next_page")
    @Expose
    var nextPage: Int? = null

    @SerializedName("playable")
    @Expose
    var playable: Int? = null

    protected constructor(`in`: Parcel) {
        status = `in`.readValue(Int::class.java.classLoader) as Int?
        message = `in`.readValue(String::class.java.classLoader) as String?
        data?.let { `in`.readList(it, EpisodeDatumModel::class.java.classLoader) }
        nextPage = `in`.readValue(Int::class.java.classLoader) as Int?
        playable = `in`.readValue(Int::class.java.classLoader) as Int?
        show_info = `in`.readValue(Int::class.java.classLoader) as String?
        censor_rating = `in`.readValue(Int::class.java.classLoader) as String?
        language_code = `in`.readValue(Int::class.java.classLoader) as String?
        release_year = `in`.readValue(Int::class.java.classLoader) as String?
        poster_tv = `in`.readValue(Int::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(message)
        dest.writeList(data)
        dest.writeValue(nextPage)
        dest.writeValue(playable)
        dest.writeValue(show_info)
        dest.writeValue(censor_rating)
        dest.writeValue(language_code)
        dest.writeValue(release_year)
        dest.writeValue(poster_tv)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<EpisodeModel> {
        override fun createFromParcel(parcel: Parcel): EpisodeModel {
            return EpisodeModel(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeModel?> {
            return arrayOfNulls(size)
        }
    }
}