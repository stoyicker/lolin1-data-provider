package org.lolin1.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.lolin1.models.champion.Champion;
import org.lolin1.utils.Utils;

public abstract class DataUpdater {

	private static final String REALM_PLACE_HOLDER = "LOWERCASE_REALM_HERE",
			LOCALE_PLACE_HOLDER = "LOCALE_MIXED_CASE_HERE",
			REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
			ALL_CHAMPIONS_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/champion?locale="
					+ DataUpdater.LOCALE_PLACE_HOLDER
					+ "&champData=lore,tags,stats,spells,passive,image",
			VERSION_KEY = "dd", CDN_KEY = "cdn";
	private static String CDN;

	@SuppressWarnings("unchecked")
	private static void performUpdate(String realm, String locale,
			String newVersion) {
		Utils.createImagesDirectory();
		String IMAGES_URL = DataUpdater.CDN + "/" + newVersion + "/img/";
		// TODO Update IMAGES_URL and new version, and replace in images_url the
		// new version
		List<Champion> champions = new ArrayList<>();
		Map<String, Object> map = (Map<String, Object>) ((Map<String, Object>) JSON
				.parse(Utils.performRiotGet(DataUpdater.ALL_CHAMPIONS_URL
						.replace(DataUpdater.REALM_PLACE_HOLDER, realm)
						.replace(DataUpdater.LOCALE_PLACE_HOLDER, locale))))
				.get("data");
		for (String key : map.keySet()) {
			Champion thisChampion = null;
			champions.add(thisChampion = new Champion(
					(HashMap<String, Object>) map.get(key)));
			Utils.downloadChampionBustImage(thisChampion, IMAGES_URL);
			Utils.downloadChampionPassiveImage(thisChampion, IMAGES_URL);
			// TODO
			// Utils.downloadChampionSpellImages(thisChampion,IMAGES_URL);
		}
		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	private static String retrieveDragonMagicVersion(String realm) {
		String ret = "", wholeFile = Utils
				.performRiotGet(DataUpdater.REALM_FILE_URL.replace(
						DataUpdater.REALM_PLACE_HOLDER, realm.toLowerCase()));
		Map<String, String> realmJson = (Map<String, String>) JSON
				.parse(wholeFile);
		String version = realmJson.get(DataUpdater.VERSION_KEY);
		DataUpdater.CDN = realmJson.get(DataUpdater.CDN_KEY);
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
					DataUpdater.performUpdate(realm, locale, newVersion);
				}
				DataAccessObject.setVersion(realm, newVersion);
			}
		}
	}
}
