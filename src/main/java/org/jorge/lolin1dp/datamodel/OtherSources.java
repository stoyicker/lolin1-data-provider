package org.jorge.lolin1dp.datamodel;

import java.net.MalformedURLException;
import java.net.URL;

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
