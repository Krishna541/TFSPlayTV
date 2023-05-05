package `in`.fiberstory.tfsplaytv.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PlexigoAuthInterceptor     //
    (private val APIVersion: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", APIInterface.xApiKey)
            .addHeader("X-version", APIVersion).build()
        return chain.proceed(authenticatedRequest)
    }
}