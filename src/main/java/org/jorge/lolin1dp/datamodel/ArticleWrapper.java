package org.jorge.lolin1dp.datamodel;

public class ArticleWrapper {

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

}
