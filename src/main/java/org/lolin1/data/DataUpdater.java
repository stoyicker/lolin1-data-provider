package org.lolin1.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.lolin1.models.Champion;
import org.lolin1.utils.Utils;

public abstract class DataUpdater {

	private static final String REALM_PLACE_HOLDER = "LOWERCASE_REALM_HERE",
			CHAMPION_KEY_PLACE_HOLDER = "CHAMPION_NUMERIC_KEY_HERE",
			LOCALE_PLACE_HOLDER = "LOCALE_MIXED_CASE_HERE",
			REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
			ALL_CHAMPIONS_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/champion?locale="
					+ DataUpdater.LOCALE_PLACE_HOLDER + "&champData=info",
			SINGLE_CHAMPION_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER
					+ "/v1/champion/"
					+ DataUpdater.CHAMPION_KEY_PLACE_HOLDER
					+ "?locale="
					+ DataUpdater.LOCALE_PLACE_HOLDER
					+ "&champData=lore,tags,stats,spells,passive",
			VERSION_KEY = "dd";

	@SuppressWarnings("unchecked")
	private static void performUpdate(String realm, String locale) {
		List<String> championKeys = new ArrayList<>();
		List<Champion> champions = new ArrayList<>();
		Map<String, Map<String, Map<String, String>>> allChampions = (Map<String, Map<String, Map<String, String>>>) JSON
				.parse(Utils.performRiotGet(DataUpdater.ALL_CHAMPIONS_URL
						.replace(DataUpdater.REALM_PLACE_HOLDER, realm)
						.replace(DataUpdater.LOCALE_PLACE_HOLDER, locale)));
		for (String key : allChampions.get("data").keySet()) {
			for (String inner : allChampions.get("data").get(key).keySet()) {
				if (inner.contentEquals("key")) {
					championKeys.add(allChampions.get("data").get(key)
							.get("key"));
					break;
				}
			}
		}
		for (String key : championKeys) {
			String championDescriptor = Utils
					.performRiotGet(DataUpdater.SINGLE_CHAMPION_URL
							.replace(DataUpdater.REALM_PLACE_HOLDER, realm)
							.replace(DataUpdater.LOCALE_PLACE_HOLDER, locale)
							.replace(DataUpdater.CHAMPION_KEY_PLACE_HOLDER, key));
			champions.add(new Champion(championDescriptor));
			// TODO Download images
			System.exit(-1);// TODO Remove this
		}
	}

	@SuppressWarnings("unchecked")
	private static String retrieveDragonMagicVersion(String realm) {
		String ret = "", wholeFile = Utils
				.performRiotGet(DataUpdater.REALM_FILE_URL.replace(
						DataUpdater.REALM_PLACE_HOLDER, realm.toLowerCase()));
		Map<String, String> realmJson = (Map<String, String>) JSON
				.parse(wholeFile);
		String version = realmJson.get(DataUpdater.VERSION_KEY);
		ret = (version == null) ? "" : version;
		return ret;
	}

	public static void updateData() {
		for (String realm : DataAccessObject.getSupportedRealms().keySet()) {
			String newVersion;
			if (!DataAccessObject.getVersion(realm)
					.contentEquals(
							(newVersion = DataUpdater
									.retrieveDragonMagicVersion(realm)))) {
				for (String locale : DataAccessObject.getSupportedRealms().get(
						realm)) {
					DataUpdater.performUpdate(realm, locale);
				}
			}
			DataAccessObject.setVersion(realm, newVersion);
		}
		System.exit(-1);
	}
}
