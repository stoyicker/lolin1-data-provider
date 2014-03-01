/**
 * This file is part of lolin1-data-provider.

    lolin1-data-provider is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    lolin1-data-provider is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lolin1.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lolin1.control.Controller;
import org.lolin1.models.champion.Champion;

public abstract class DataAccessObject {

	private final static String RESPONSE_UNSUPPORTED = "{\"status\":\"unsupported\"}",
			RESPONSE_ERROR = "{\"status\":\"error\"}";
	private static Map<String, String> CHAMPIONS_VERSION_MAP = new HashMap<>();
	private static final Map<String, String[]> SUPPORTED_REALMS = new HashMap<>();

	private static String formatChampionListAsJSON(List<Champion> champions) {
		StringBuilder ret = new StringBuilder("[");
		for (Iterator<Champion> it = champions.iterator(); it.hasNext();) {
			ret.append(it.next().toString());
			if (it.hasNext()) {
				ret.append(",");
			}
		}
		return ret.append("]").toString();
	}

	public static final String getJSONChampions(String realm, String locale) {
		String ret;
		if (!DataUpdater.isUpdating()) {
			Controller controller = Controller.getController();
			if (controller.isPairSupported(locale, realm)) {
				List<Champion> champions = controller.getChampions(locale,
						realm);
				ret = DataAccessObject.formatChampionListAsJSON(champions);
			} else {
				ret = DataAccessObject.RESPONSE_UNSUPPORTED;
			}
		} else {
			ret = DataAccessObject.getResponseError();
		}

		return ret;
	}

	public static final String getJSONVersion(String realm) {
		StringBuffer ret;
		if (!DataUpdater.isUpdating()) {
			if (!DataAccessObject.CHAMPIONS_VERSION_MAP.containsKey(realm)) {
				ret = new StringBuffer(DataAccessObject.getResponseError());
			} else {
				ret = new StringBuffer("{\"status\":\"ok\", \"version\":\""
						+ DataAccessObject.CHAMPIONS_VERSION_MAP.get(realm)
						+ "\"}");
			}
		} else {
			ret = new StringBuffer(DataAccessObject.getResponseError());
		}
		return ret.toString();
	}

	public static String getResponseError() {
		return DataAccessObject.RESPONSE_ERROR;
	}

	public static Map<String, String[]> getSupportedRealms() {
		return DataAccessObject.SUPPORTED_REALMS;
	}

	public static final String getVersion(String realm) {
		return DataAccessObject.CHAMPIONS_VERSION_MAP.get(realm);
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
			// As well, put a blank version to force the download of such data
			DataAccessObject.CHAMPIONS_VERSION_MAP.put(realm, "");
		}
	}

	protected static final void setChampionsVersion(String realm,
			String newVersion) {
		DataAccessObject.CHAMPIONS_VERSION_MAP.put(realm, newVersion);
	}
}
