package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailResponseModel(
    @SerializedName("statusCode") @Expose
     var statusCode: String? = null,

    @SerializedName("status")
    @Expose
     val status: String? = null,

    @SerializedName("data")
    @Expose
     val data: MovieDetailResponseDataModel? = MovieDetailResponseDataModel()
)
