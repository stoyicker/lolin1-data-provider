package org.jorge.lolin1dp.io.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jorge.lolin1dp.datamodel.ArticleWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class Internet {

	public static final String ERROR = "ERROR";

	public static List<ArticleWrapper> getNews(String baseUrl, String url) {
		Elements newsHeadLines;
		try {
			System.out.println("Performing get on " + url);
			Document doc = Jsoup.connect(url).get();
			System.out.println("Get performed on " + url);
			newsHeadLines = doc.select("div.panelizer-view-mode")
					.select("div.node").select("div.node-teaser")
					.select("div.node-article").select("div.field")
					.select("div.field-name-field-article-media")
					.select("div.field-type-file")
					.select("div.field-label-hidden");
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return null;
		}

		final List<ArticleWrapper> ret = new ArrayList<>();
		Boolean addThis = Boolean.TRUE;
		for (Element elem : newsHeadLines) {
			Element linkElem = elem.getElementsByTag("a").first(), imageElem = elem
					.getElementsByTag("img").first();

			if (addThis) {
				final String title = linkElem.attr("title");
				final String link = baseUrl + linkElem.attr("href");
				final String imageLink = baseUrl + imageElem.attr("src");
				ret.add(new ArticleWrapper(title, link, imageLink));
				addThis = Boolean.FALSE;
			} else {
				addThis = Boolean.TRUE;
			}
		}

		return ret;
	}
}
