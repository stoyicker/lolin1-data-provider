package org.jorge.lolin1dp.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jorge.lolin1dp.datamodel.ArticleWrapper;
import org.jorge.lolin1dp.datamodel.Realm;
import org.jorge.lolin1dp.datamodel.Realm.RealmEnum;
import org.jorge.lolin1dp.io.file.FileUtils;
import org.jorge.lolin1dp.io.net.Internet;
import org.json.JSONArray;

public abstract class DataUpdater {

	private static final Path FILES_PATH = Paths.get("files"),
			COMMUNITY_PATH = Paths.get(FILES_PATH.toString(), "COMMUNITY"),
			SCHOOL_PATH = Paths.get(FILES_PATH.toString(), "SCHOOL");

	public synchronized static void initFileStructure() {
		// TODO initFileStructure
		if (!Files.exists(FILES_PATH)) {
			if (!FILES_PATH.toFile().mkdirs())
				throw new IllegalStateException("Could not create files folder");
		}

		final RealmEnum[] values = RealmEnum.values();
		for (RealmEnum x : values) {
			List<String> localesInThisRealm = Realm.getLocales(x);
			for (String y : localesInThisRealm) {
				FileUtils.createJSONFileIfNotExists(getRealmFilePath(x, y));
			}
		}

		FileUtils.createJSONFileIfNotExists(COMMUNITY_PATH);
		FileUtils.createJSONFileIfNotExists(SCHOOL_PATH);
	}

	public synchronized static void updateData() {
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

				FileUtils.writeFile(getRealmFilePath(realm, locale),
						array.toString());
			}
		}
	}

	private static Path getRealmFilePath(RealmEnum realmId, String locale) {
		return Paths.get(FILES_PATH.toString(), realmId.name() + "_"
				+ (locale != null ? locale : "") + ".json");
	}
}
