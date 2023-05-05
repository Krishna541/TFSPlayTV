package `in`.fiberstory.tfsplaytv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentTokenResModel (
    @SerializedName("statusCode")
    val statusCode: String? = null,

    @SerializedName("status")
    @Expose
     val status: String? = null,

    @SerializedName("contentToken")
    @Expose
     val contentToken: String? = null,

    @SerializedName("message")
    @Expose
     val message: String? = null,

    @SerializedName("dashURL")
    @Expose
     val dashURL: String? = null,

    @SerializedName("widevineLicenseURL")
    @Expose
     val widevineLicenseURL: String? = null,

    @SerializedName("hlsURL")
    @Expose
     val hlsURL: String? = null,

    @SerializedName("fairplayLicenseURL")
    @Expose
     val fairplayLicenseURL: String? = null,

    @SerializedName("fairplayCertificateURL")
    @Expose
     val fairplayCertificateURL: String? = null
)