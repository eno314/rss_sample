package jp.eno314.rssreader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import jp.eno314.rssreader.rss.Parser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                // .baseUrl("https://dev.classmethod.jp/")
                // .baseUrl("http://jp.techcrunch.com/")
                .baseUrl("https://codeiq.jp/magazine/")
                .build()
        val service = retrofit.create(RssService::class.java)
                // .get("https://dev.classmethod.jp/feed/")
                // .get("http://jp.techcrunch.com/feed/")
                .get("https://codeiq.jp/magazine/feed/")

        service.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val xmlString = response?.body()?.string()
                if (xmlString != null) {
                    Parser().execute(xmlString)
                } else {
                    onFailure(call, IllegalStateException("response body is null"))
                }
            }
        })
    }

    interface RssService {
        @GET
        fun get(@Url url: String): Call<ResponseBody>
    }
}
