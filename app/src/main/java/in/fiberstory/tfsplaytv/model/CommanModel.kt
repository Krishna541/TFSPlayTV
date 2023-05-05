package `in`.fiberstory.tfsplaytv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel

class CommanModel : Parcelable {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null


    protected constructor(`in`: Parcel) {
        status = `in`.readValue(Int::class.java.classLoader) as Int?
        message = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(message)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<CommanModel> {
        override fun createFromParcel(parcel: Parcel): CommanModel {
            return CommanModel(parcel)
        }

        override fun newArray(size: Int): Array<CommanModel?> {
            return arrayOfNulls(size)
        }
    }
}