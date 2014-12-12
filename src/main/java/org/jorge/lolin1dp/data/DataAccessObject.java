package org.jorge.lolin1dp.data;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jorge.lolin1dp.datamodel.Realm.RealmEnum;
import org.jorge.lolin1dp.io.file.FileUtils;

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
public abstract class DataAccessObject {

	static final Path FILES_PATH = Paths.get("files"), COMMUNITY_PATH = Paths
			.get(FILES_PATH.toString(), "COMMUNITY"), SCHOOL_PATH = Paths.get(
			FILES_PATH.toString(), "SCHOOL");

	static Path getRealmFilePath(RealmEnum realmId, String locale) {
		return Paths
				.get(DataAccessObject.FILES_PATH.toString(), (realmId.name()
						+ "_" + (locale != null ? locale : "") + ".json")
						.toLowerCase());
	}

	static Path getCommunityFilePath() {
		return COMMUNITY_PATH;
	}

	static Path getSchoolFilePath() {
		return SCHOOL_PATH;
	}

	public static String getJSONNewsAsString(RealmEnum realm, String locale) {
		System.out.println("File requested " + getRealmFilePath(realm, locale));
		System.out.println("Responded: "
				+ FileUtils.readFile(getRealmFilePath(realm, locale)));
		return FileUtils.readFile(getRealmFilePath(realm, locale));
	}

	public static String getJSONCommunityAsString() {
		System.out.println("File requested " + getCommunityFilePath());
		System.out.println("Responded: "
				+ FileUtils.readFile(getCommunityFilePath()));
		return FileUtils.readFile(getCommunityFilePath());
	}

	public static String getJSONSchoolAsString() {
		System.out.println("File requested " + getSchoolFilePath());
		System.out.println("Responded: "
				+ FileUtils.readFile(getSchoolFilePath()));
		return FileUtils.readFile(getSchoolFilePath());
	}
}
