package org.lolin1.data;

import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.lolin1.utils.Utils;

public abstract class DataUpdater {

	private static final String REALM_PLACE_HOLDER = "PUTREALMINLOWERCASEHERE",
			REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
			VERSION_KEY = "dd";

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
		// TODO FOR EACH REALM: If the map is empty or the version is different,
		// then update that realm
		for (String x : DataAccessObject.getSupportedRealms()) {
			System.out.println("Version of realm " + x + ": "
					+ DataUpdater.retrieveDragonMagicVersion(x));
		}
		System.exit(-1);
	}
}
