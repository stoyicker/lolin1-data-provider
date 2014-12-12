package org.jorge.lolin1dp.datamodel;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleWrapper {

	private static final String KEY_TITLE = "KEY_TITLE";
	private static final String KEY_CONTENT_URL = "KEY_CONTENT_URL";
	private static final String KEY_IMG_URL = "KEY_IMG_URL";
	private final String title, contentUrl, imageUrl;

	public ArticleWrapper(String _title, String _contentUrl, String _imageUrl) {
		title = _title;
		contentUrl = _contentUrl;
		imageUrl = _imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public String getTitle() {
		return title;
	}

	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put(KEY_TITLE, getTitle());
			ret.put(KEY_CONTENT_URL, getContentUrl());
			ret.put(KEY_IMG_URL, getImageUrl());
		} catch (JSONException unused) {
			// This only happens if the key is null, but it never is
		}
		return ret;
	}
}
