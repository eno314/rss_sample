package jp.eno314.rssreader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import jp.eno314.rssreader.rss.RssRepository
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI


class MainActivity : AppCompatActivity() {

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        job = launch(UI) {
            try {
                val rss = async(CommonPool) {
                    RssRepository().request("https://dev.classmethod.jp/feed/")
                }.await()
                rss?.let {
                    Log.d("BBBBB", "title : " + it.channel.title)
                    it.channel.itemList.forEach { item ->
                        Log.d("BBBBB", "item title : " + item.title)
                    }
                }
            } catch (e: CancellationException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        job?.cancel()
    }
}
