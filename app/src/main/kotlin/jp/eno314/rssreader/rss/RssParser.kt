package jp.eno314.rssreader.rss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader


class RssParser {

    fun execute(xmlString: String): Rss? {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xmlString))

        var channel: Rss.Channel? = null

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "channel") {
                channel = parseChannel(parser)
            }

            eventType = parser.next()
        }

        val nonNullChannel = channel ?: return null
        return Rss(nonNullChannel)
    }

    private fun parseChannel(parser: XmlPullParser): Rss.Channel {
        var title = ""
        var link = ""
        var lastBuildDate = ""
        var imageUrl = ""
        var description = ""
        val itemList = mutableListOf<Rss.Item>()

        var eventType = parser.next()
        while (!(eventType == XmlPullParser.END_TAG && parser.name == "channel")) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "title" -> {
                        parser.next()
                        title = parser.text
                    }
                    "link" -> {
                        parser.next()
                        link = parser.text
                    }
                    "lastBuildDate" -> {
                        parser.next()
                        lastBuildDate = parser.text
                    }
                    "imageUrl" -> {
                        parser.next()
                        imageUrl = parser.text
                    }
                    "description" -> {
                        parser.next()
                        description = parser.text
                    }
                    "item" -> {
                        val item = parseItem(parser)
                        itemList.add(item)
                    }
                }
            }

            eventType = parser.next()
        }

        return Rss.Channel(title, link, lastBuildDate, imageUrl, description, itemList)
    }

    private fun parseItem(parser: XmlPullParser): Rss.Item {
        var title = ""
        var link = ""
        var description = ""
        var pubDate = ""
        var content = ""

        var eventType = parser.next()
        while (!(eventType == XmlPullParser.END_TAG && parser.name == "item")) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "title" -> {
                        parser.next()
                        title = parser.text
                    }
                    "link" -> {
                        parser.next()
                        link = parser.text
                    }
                    "description" -> {
                        parser.next()
                        description = parser.text
                    }
                    "pubDate" -> {
                        parser.next()
                        pubDate = parser.text
                    }
                    "content:encoded" -> {
                        parser.next()
                        content = parser.text
                    }
                }
            }

            eventType = parser.next()
        }

        return Rss.Item(title, link, description, pubDate, content)
    }
}