package `in`.fiberstory.tfsplaytv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel
import android.os.Parcelable.Creator

class SearchModel : Parcelable {
    @SerializedName("movies")
    @Expose
    var movies: List<DocumentaryItemsModel?>? = null

    @SerializedName("shows")
    @Expose
    var shows: List<TVShowsDatumModel?>? = null

    @SerializedName("channels")
    @Expose
    var channels: List<LiveTVItemModel?>? = null

    @SerializedName("documentaries")
    @Expose
    var documentaries: List<DocumentaryItemsModel?>? = null

    @SerializedName("short-films")
    @Expose
    var shortfilms: List<DocumentaryItemsModel?>? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    protected constructor(`in`: Parcel) {
        movies?.let { `in`.readList(it, DocumentaryItemsModel::class.java.classLoader) }
        shows?.let { `in`.readList(it, TVShowsDatumModel::class.java.classLoader) }
        channels?.let { `in`.readList(it, LiveTVItemModel::class.java.classLoader) }
        documentaries?.let { `in`.readList(it, DocumentaryItemsModel::class.java.classLoader) }
        shortfilms?.let { `in`.readList(it, DocumentaryItemsModel::class.java.classLoader) }
        status = `in`.readValue(Int::class.java.classLoader) as Int?
        message = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(movies)
        dest.writeList(shows)
        dest.writeList(channels)
        dest.writeList(documentaries)
        dest.writeList(shortfilms)
        dest.writeValue(status)
        dest.writeValue(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<SearchModel> {
        override fun createFromParcel(parcel: Parcel): SearchModel {
            return SearchModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchModel?> {
            return arrayOfNulls(size)
        }
    }
}