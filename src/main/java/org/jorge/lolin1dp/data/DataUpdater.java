package org.jorge.lolin1dp.data;

import java.nio.file.Files;
import java.util.List;

import org.jorge.lolin1dp.datamodel.ArticleWrapper;
import org.jorge.lolin1dp.datamodel.OtherSources;
import org.jorge.lolin1dp.datamodel.Realm;
import org.jorge.lolin1dp.datamodel.Realm.RealmEnum;
import org.jorge.lolin1dp.io.file.FileUtils;
import org.jorge.lolin1dp.io.net.Internet;
import org.json.JSONArray;

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
public abstract class DataUpdater {

	private static Boolean updating = Boolean.TRUE;

	public synchronized static void initFileStructure() {
		if (!Files.exists(DataAccessObject.FILES_PATH)) {
			if (!DataAccessObject.FILES_PATH.toFile().mkdirs())
				throw new IllegalStateException("Could not create files folder");
		}

		final RealmEnum[] values = RealmEnum.values();
		for (RealmEnum x : values) {
			List<String> localesInThisRealm = Realm.getLocales(x);
			for (String y : localesInThisRealm) {
				FileUtils.createJSONFileIfNotExists(DataAccessObject
						.getRealmFilePath(x, y));
			}
		}

		FileUtils.createJSONFileIfNotExists(DataAccessObject.COMMUNITY_PATH);
		FileUtils.createJSONFileIfNotExists(DataAccessObject.SCHOOL_PATH);
	}

	public synchronized static void updateData() {
		updating = Boolean.TRUE;

		final RealmEnum[] values = RealmEnum.values();
		for (RealmEnum realm : values) {

			List<ArticleWrapper> news;
			List<String> realmLocales = Realm.getLocales(realm);

			for (String locale : realmLocales) {
				news = Internet.getNews(Realm.getBaseUrl(realm),
						Realm.getNewsUrl(realm, locale));

				JSONArray array = new JSONArray();

				for (ArticleWrapper article : news)
					array.put(article.toJSON());

				FileUtils.writeFile(
						DataAccessObject.getRealmFilePath(realm, locale),
						array.toString());
			}
		}

		Internet.getSubrreditHot(OtherSources.getMaxAmountToPull(),
				OtherSources.getCommunityUrl(), "hola");

		updating = Boolean.FALSE;
	}

	public static Boolean isUpdating() {
		return updating;
	}
}
