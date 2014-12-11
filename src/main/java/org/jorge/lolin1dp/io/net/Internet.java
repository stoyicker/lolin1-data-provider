package org.jorge.lolin1dp.io.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class Internet {

	public static final String ERROR = "ERROR";

	public static List<String> getNews(String url) {
		Elements newsHeadLines;
		try {
			Document doc = Jsoup.connect(url).get();
			newsHeadLines = doc.select("div.panelizer-view-mode")
					.select("div.node").select("div.node-teaser")
					.select("div.node-article");
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return null;
		}

		final Object[] arr = newsHeadLines.toArray();

		final List<String> ret = new ArrayList<>();
		for (Object x : arr) {
			ret.add(x.toString());
		}

		return ret;
	}
}
