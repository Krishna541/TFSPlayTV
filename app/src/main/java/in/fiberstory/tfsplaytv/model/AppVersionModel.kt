package `in`.fiberstory.tfsplaytv.model



import com.google.gson.annotations.SerializedName

data class AppVersionModel(
    @SerializedName("data")  val `data`: ArrayList<Data> ,
//    @SerializedName("data")  val `data`: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Int? = null
)



data class Data(
    @SerializedName("apk_url")  val apk_url: String? = null,
    @SerializedName("app_name") val app_name: String? = null,
    @SerializedName("app_version") val app_version: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("mandatory") val mandatory: String? = null,
    @SerializedName("package_id") val package_id: String? = null
)