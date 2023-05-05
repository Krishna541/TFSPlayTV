package `in`.fiberstory.tfsplaytv.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


class LoginModel : Parcelable {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("login")
    @Expose
    var login: LoginDataModel? = null

    @SerializedName("login_devices")
    @Expose
    var loginDevices: List<LoginDeviceModel?>? = null

    protected constructor(`in`: Parcel) {
        status = `in`.readValue(Int::class.java.classLoader) as Int?
        message = `in`.readValue(String::class.java.classLoader) as String?
        login = `in`.readValue(LoginDataModel::class.java.classLoader) as LoginDataModel?
        `in`.readList(loginDevices!!, LoginDeviceModel::class.java.classLoader)
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(message)
        dest.writeValue(login)
        dest.writeList(loginDevices)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<LoginModel> {
        override fun createFromParcel(parcel: Parcel): LoginModel {
            return LoginModel(parcel)
        }

        override fun newArray(size: Int): Array<LoginModel?> {
            return arrayOfNulls(size)
        }
    }
}