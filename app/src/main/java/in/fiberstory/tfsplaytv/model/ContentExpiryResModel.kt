package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentExpiryResModel(
    @SerializedName("validationStatus")
    @Expose
    var validationStatus: String? = null
)
