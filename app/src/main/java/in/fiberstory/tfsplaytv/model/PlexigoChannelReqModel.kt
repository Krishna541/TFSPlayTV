package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlexigoChannelReqModel(

    @SerializedName("Mobileno")
    @Expose
    var Mobileno: String? = null
)

