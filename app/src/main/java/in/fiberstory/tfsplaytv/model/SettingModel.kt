package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.SerializedName


data class SettingModel(
    @SerializedName("Id") val Id:Int? = null,
    @SerializedName("Icon") val Icon: Int? = null,
    @SerializedName("Name") val Name: String? = ""
)
