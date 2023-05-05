package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.SerializedName

data class PlexigoUserExistApiResponseModel(

    @SerializedName("statusCode") var statusCode: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var data: UserData? = UserData()
)

data class User(
    @SerializedName("userId") var userId: Int? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null
)

data class UserData(
    @SerializedName("message") var message: String? = null,
    @SerializedName("user") var user: ArrayList<User> = arrayListOf()
)