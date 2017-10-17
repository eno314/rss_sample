package jp.eno314.rssreader.rss

data class Rss(
        val channel: Channel
) {
    data class Channel(
            val title: String,
            val link: String,
            val lastBuildDate: String,
            val imageUrl: String,
            val description: String,
            val itemList: List<Item>
    )

    data class Item(
            val title: String,
            val link: String,
            val description: String,
            val pubDate: String,
            val content: String
    )
}
