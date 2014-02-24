package org.lolin1.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lolin1.control.ChampionManager;
import org.lolin1.models.champion.Champion;

public abstract class DataAccessObject {

	private final static String RESPONSE_UNSUPPORTED = "{\"status\":\"unsupported\"}",
			RESPONSE_ERROR = "{\"status\":\"error\"}";
	private static Map<String, String> VERSION_MAP = new HashMap<String, String>();
	private static final Map<String, String[]> SUPPORTED_REALMS = new HashMap<>();

	private static String formatChampionListAsJSON(List<Champion> champions) {
		StringBuilder ret = new StringBuilder("{[");
		for (Iterator<Champion> it = champions.iterator(); it.hasNext();) {
			ret.append(it.next().toString());
			if (it.hasNext()) {
				ret.append(",");
			}
		}
		return ret.append("]}").toString();
	}

	public static final String getJSONChampions(String realm, String locale) {
		String ret;
		if (!DataUpdater.isUpdating()) {
			ChampionManager championManager = ChampionManager
					.getChampionManager();
			if (championManager.isPairSupported(locale, realm)) {
				List<Champion> champions = championManager.getChampions(locale,
						realm);
				ret = DataAccessObject.formatChampionListAsJSON(champions);
			} else {
				ret = DataAccessObject.RESPONSE_UNSUPPORTED;
			}
		} else {
			ret = DataAccessObject.RESPONSE_ERROR;
		}

		return ret;
	}

	public static final String getJSONVersion(String realm) {
		StringBuffer ret;
		if (!DataUpdater.isUpdating()) {
			if (!DataAccessObject.VERSION_MAP.containsKey(realm)) {
				ret = new StringBuffer(DataAccessObject.RESPONSE_ERROR);
			} else {
				ret = new StringBuffer("{\"status\":\"ok\", \"version\":\""
						+ DataAccessObject.VERSION_MAP.get(realm) + "\"}");
			}
		} else {
			ret = new StringBuffer(DataAccessObject.RESPONSE_ERROR);
		}
		return ret.toString();
	}

	public static Map<String, String[]> getSupportedRealms() {
		return DataAccessObject.SUPPORTED_REALMS;
	}

	public static final String getVersion(String realm) {
		return DataAccessObject.VERSION_MAP.get(realm);
	}

	public static final void initDAO() {
		// Upon server start there's no data on dynamic memory, load the realms
		// and their corresponding locale codes
		DataAccessObject.SUPPORTED_REALMS.put("euw", new String[] { "en_US",
				"de_DE", "es_ES", "fr_FR", "it_IT" });
		DataAccessObject.SUPPORTED_REALMS.put("eune", new String[] { "en_US",
				"el_GR", "pl_PL", "ro_RO" });
		DataAccessObject.SUPPORTED_REALMS.put("na", new String[] { "en_US" });
		DataAccessObject.SUPPORTED_REALMS.put("tr", new String[] { "tr_TR" });
		DataAccessObject.SUPPORTED_REALMS.put("br", new String[] { "pt_PT" });

		for (String realm : DataAccessObject.getSupportedRealms().keySet()) {
			DataAccessObject.VERSION_MAP.put(realm, "");// As well, put blank
														// version to
														// force downloading
														// such data
		}
	}

	public static final void setVersion(String realm, String newVersion) {
		DataAccessObject.VERSION_MAP.put(realm, newVersion);
	}
}
