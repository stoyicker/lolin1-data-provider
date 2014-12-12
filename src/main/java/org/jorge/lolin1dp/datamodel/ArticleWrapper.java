package org.jorge.lolin1dp.datamodel;

import org.json.JSONException;
import org.json.JSONObject;

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

	@Override
	public String toString() {
		return "Title: " + getTitle() + "--Content url: " + getContentUrl()
				+ "--Image url: " + getImageUrl();
	}
}
