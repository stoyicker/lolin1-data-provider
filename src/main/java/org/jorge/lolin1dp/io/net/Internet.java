package org.jorge.lolin1dp.io.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jorge.lolin1dp.datamodel.ArticleWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * This file is part of lolin1dp-data-provider.
 * <p/>
 * lolin1dp-data-provider is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * <p/>
 * lolin1dp-data-provider is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * lolin1dp-data-provider. If not, see <http://www.gnu.org/licenses/>.
 */
public abstract class Internet {

	public static final String ERROR = "ERROR";
	private static final Integer URL_TIMEOUT_MILLIS = 60000;
	private static final OkHttpClient client = new OkHttpClient();

	public static List<ArticleWrapper> getNews(String baseUrl, String url) {
		Elements newsHeadLines, newsSubTitles, descVerification;
		try {
			System.out.println("Performing get on " + url);
			Document doc = Jsoup.connect(url).timeout(URL_TIMEOUT_MILLIS).get();
			System.out.println("Get performed on " + url);
			newsHeadLines = doc.select("div.panelizer-view-mode")
					.select("div.node").select("div.node-teaser")
					.select("div.node-article").select("div.field")
					.select("div.field-name-field-article-media")
					.select("div.field-type-file")
					.select("div.field-label-hidden");
			newsSubTitles = doc.select("div.field")
					.select("div.field-name-field-body-medium")
					.select("div.field-type-text-long")
					.select("div.field-label-hidden");
			descVerification = doc.select("div.default-2-3");
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return new ArrayList<ArticleWrapper>();
		}

		final List<ArticleWrapper> ret = new ArrayList<>();
		Boolean addThis = Boolean.TRUE;
		int i = 0;
		for (Element elem : newsHeadLines) {

			Element linkElem = elem.getElementsByTag("a").first(), imageElem = elem
					.getElementsByTag("img").first();

			if (addThis) {
				final String title = linkElem.attr("title");
				final String link = baseUrl + linkElem.attr("href");
				final String imageLink = baseUrl + imageElem.attr("src");
				final String subtitle;
				if (descVerification.get(i).select("div").size() < 7) {
					subtitle = "";
				} else {
					Element removed = newsSubTitles.remove(0);
					subtitle = removed.text();
				}
				ret.add(new ArticleWrapper(title, link, imageLink, subtitle));
				addThis = Boolean.FALSE;
				i++;
			} else {
				addThis = Boolean.TRUE;
			}
		}

		return ret;
	}

	public static List<ArticleWrapper> getSubrreditHot(int howMany,
			String subredditUrl, String defaultImgUrl) {
		JSONArray array;
		List<ArticleWrapper> ret = new ArrayList<>();

		try {
			System.out.println("Performing get on " + subredditUrl);
			String response = doGetAsString(subredditUrl);
			System.out.println("Get performed on " + subredditUrl);
			array = new JSONObject(response).getJSONObject("data")
					.getJSONArray("children");
			for (int i = 0; i < array.length() && ret.size() < howMany; i++) {
				JSONObject object = array.getJSONObject(i)
						.getJSONObject("data");
				if (!object.getBoolean("stickied")) // We don't want sticky
													// posts
					ret.add(new ArticleWrapper(
							object.getString("title"),
							object.getString("url"),
							object.getString("thumbnail").contentEquals("self") ? defaultImgUrl
									: object.getString("thumbnail"), object
									.getString("selftext_html")));
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace(System.out);
			return new ArrayList<ArticleWrapper>();
		}

		return ret;
	}

	private static String doGetAsString(String url) throws IOException {
		client.setConnectTimeout(URL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}
}
