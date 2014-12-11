package org.jorge.lolin1dp.datamodel;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class OtherSources {

	private static final String COMMUNITY_URL = "http://www.reddit.com/r/leagueoflegends",
			SCHOOL_URL = "http://www.reddit.com/r/summonerschool";

	public static URL getCommunityURL() {
		try {
			return new URL(COMMUNITY_URL);
		} catch (MalformedURLException e) {
			// Unused
			throw new IllegalArgumentException("Unused");
		}
	}

	public static URL getSchoolURL() {
		try {
			return new URL(SCHOOL_URL);
		} catch (MalformedURLException e) {
			// Unused
			throw new IllegalArgumentException("Unused");
		}
	}

}
