package jp.eno314.rssreader.rss

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class RssRepository {

    fun request(url: String): Rss? {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient().newBuilder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        val responseBody = response.body() ?: return null
        return RssParser().execute(responseBody.string())
    }
}