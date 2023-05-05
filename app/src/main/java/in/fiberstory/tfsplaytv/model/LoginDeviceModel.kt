package `in`.fiberstory.tfsplaytv.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class LoginDeviceModel : Parcelable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("subscriber_id")
    @Expose
    var subscriberId: String? = null

    @SerializedName("mobile_device_id")
    @Expose
    var mobileDeviceId: String? = null

    @SerializedName("mobile_device_name")
    @Expose
    var mobileDeviceName: String? = null

    @SerializedName("fcm_id")
    @Expose
    var fcmId: String? = null

    protected constructor(`in`: Parcel) {
        id = `in`.readValue(String::class.java.classLoader) as String?
        subscriberId = `in`.readValue(String::class.java.classLoader) as String?
        mobileDeviceId = `in`.readValue(String::class.java.classLoader) as String?
        mobileDeviceName = `in`.readValue(String::class.java.classLoader) as String?
        fcmId = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(subscriberId)
        dest.writeValue(mobileDeviceId)
        dest.writeValue(mobileDeviceName)
        dest.writeValue(fcmId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginDeviceModel> {
        override fun createFromParcel(parcel: Parcel): LoginDeviceModel {
            return LoginDeviceModel(parcel)
        }

        override fun newArray(size: Int): Array<LoginDeviceModel?> {
            return arrayOfNulls(size)
        }
    }
}