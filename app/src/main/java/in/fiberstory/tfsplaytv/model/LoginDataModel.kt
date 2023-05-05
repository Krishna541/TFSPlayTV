package `in`.fiberstory.tfsplaytv.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class LoginDataModel : Parcelable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("subscriber_id")
    @Expose
    var subscriberId: String? = null

    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mobile_no")
    @Expose
    var mobileNo: String? = null

    @SerializedName("gender_id")
    @Expose
    var genderId: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("current_balance")
    @Expose
    var currentBalance: String? = null

    @SerializedName("pay_channels")
    @Expose
    var payChannels: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("profile_pic")
    @Expose
    var profilePic: String? = null

    protected constructor(`in`: Parcel) {
        id = `in`.readValue(String::class.java.classLoader) as String?
        subscriberId = `in`.readValue(String::class.java.classLoader) as String?
        deviceId = `in`.readValue(String::class.java.classLoader) as String?
        email = `in`.readValue(String::class.java.classLoader) as String?
        mobileNo = `in`.readValue(String::class.java.classLoader) as String?
        genderId = `in`.readValue(String::class.java.classLoader) as String?
        dob = `in`.readValue(String::class.java.classLoader) as String?
        currentBalance = `in`.readValue(String::class.java.classLoader) as String?
        payChannels = `in`.readValue(String::class.java.classLoader) as String?
        name = `in`.readValue(String::class.java.classLoader) as String?
        profilePic = `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(subscriberId)
        dest.writeValue(deviceId)
        dest.writeValue(email)
        dest.writeValue(mobileNo)
        dest.writeValue(genderId)
        dest.writeValue(dob)
        dest.writeValue(currentBalance)
        dest.writeValue(payChannels)
        dest.writeValue(name)
        dest.writeValue(profilePic)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<LoginDataModel> {
        override fun createFromParcel(parcel: Parcel): LoginDataModel {
            return LoginDataModel(parcel)
        }

        override fun newArray(size: Int): Array<LoginDataModel?> {
            return arrayOfNulls(size)
        }
    }
}