package org.lolin1.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lolin1.control.ChampionManager;
import org.lolin1.models.Champion;

public abstract class DataAccessObject {

	private final static String RESPONSE_UNSUPPORTED = "{\"status\":\"unsupported\"}",
			RESPONSE_ERROR = "{\"status\":\"error\"}";
	private static Map<String, String> VERSION_MAP = new HashMap<String, String>();
	private static final Object champsLock = new Object(),
			versionLock = new Object();
	private static final String[] supportedRealms = new String[] { "euw",
			"eune", "na", "tr", "br" };

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
		synchronized (DataAccessObject.champsLock) {
			ChampionManager championManager = ChampionManager
					.getChampionManager();
			if (championManager.isPairSupported(locale, realm)) {
				List<Champion> champions = championManager.getChampions(locale,
						realm);
				ret = DataAccessObject.formatChampionListAsJSON(champions);
			} else {
				ret = DataAccessObject.RESPONSE_UNSUPPORTED;
			}

			return ret;
		}
	}

	public static final String getJSONVersion(String realm) {
		StringBuffer ret;
		synchronized (DataAccessObject.versionLock) {
			if (!DataAccessObject.VERSION_MAP.containsKey(realm)) {
				ret = new StringBuffer(DataAccessObject.RESPONSE_ERROR);
			} else {
				ret = new StringBuffer("{\"status\":\"ok\", \"version\":\""
						+ DataAccessObject.VERSION_MAP.get(realm) + "\"}");
			}
			return ret.toString();
		}
	}

	public static String[] getSupportedRealms() {
		return DataAccessObject.supportedRealms;
	}

	public static final String getVersion(String realm) {
		return DataAccessObject.VERSION_MAP.get(realm);
	}

	public static final void setVersion(String realm, String newVersion) {
		synchronized (DataAccessObject.versionLock) {
			DataAccessObject.VERSION_MAP.put(realm, newVersion);
		}
	}
}
