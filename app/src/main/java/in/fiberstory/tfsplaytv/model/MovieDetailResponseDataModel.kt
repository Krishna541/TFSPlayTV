package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponseDataModel(
    @SerializedName("contentdetail")
    val contentdetail: ArrayList<MovieDetailModel> =  arrayListOf(),

    @SerializedName("message")
    val message: String ?= null

)
