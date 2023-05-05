package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlexigoDoRegisterRequestModel(


    @SerializedName("UserName")
    @Expose
    var UserName: String? = null,

    @SerializedName("EmailId")
    @Expose
    var EmailId: String? = null,


    @SerializedName("Mobileno")
    @Expose
    var Mobileno: String? = null,

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null
)
