package org.jorge.lolin1dp.datamodel;

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

	private static final String COMMUNITY_HOT_URL = "http://www.reddit.com/r/leagueoflegends/hot.json",
			SCHOOL_HOT_URL = "http://www.reddit.com/r/summonerschool/hot.json";
	private static final Integer MAX_AMOUNT_TO_PULL = 8;

	public static String getCommunityUrl() {
		return COMMUNITY_HOT_URL;
	}

	public static String getSchoolUrl() {
		return SCHOOL_HOT_URL;
	}

	public static Integer getMaxAmountToPull() {
		return MAX_AMOUNT_TO_PULL;
	}

}
