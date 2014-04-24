package org.jorge.lolin1.data;

import lol4j.util.Region;
import org.eclipse.jetty.util.ajax.JSON;
import org.jorge.lolin1.control.ListManager;
import org.jorge.lolin1.models.champion.Champion;
import org.jorge.lolin1.utils.LoLin1DataProviderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file is part of lolin1-data-provider.
 * <p/>
 * lolin1-data-provider is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * lolin1-data-provider is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */

public abstract class DataUpdater {

    private static final String REALM_PLACE_HOLDER = "LOWERCASE_REALM_HERE",
            LOCALE_PLACE_HOLDER = "LOCALE_MIXED_CASE_HERE",
            REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
                    + DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
            ALL_CHAMPIONS_URL = "https://prod.api.pvp.net/api/lol/static-data/"
                    + DataUpdater.REALM_PLACE_HOLDER + "/v1/champion?locale="
                    + DataUpdater.LOCALE_PLACE_HOLDER
                    + "&champData=lore,tags,stats,spells,passive,image,skins",
            INFO_WRAPPER = "n", VERSION_KEY = "champion", CDN_KEY = "cdn";
    private static final long RETRY_DELAY_MILLIS = 300000;

    private static Boolean UPDATING = Boolean.TRUE; // If it starts being TRUE,
    // the server start-up is
    // immediate

    public static long getRetryDelayMillis() {
        return DataUpdater.RETRY_DELAY_MILLIS;
    }

    public static boolean isUpdating() {
        return DataUpdater.UPDATING;
    }

    @SuppressWarnings("unchecked")
    /**
     *
     * @param realm {@link String} Riot sometimes deploys different images (or same one with different names) depending on the realm...BECAUSE YES!
     * @param locale
     * @param newVersion
     */
    private static void performUpdate(final Region realm, String locale,
                                      String newVersion) {
        DataUpdater.UPDATING = Boolean.TRUE;
        ListManager.getInstance().createChampionListsDirectory(realm, locale);
        List<Champion> champions;
        Map<String, Object> map;
        try {
            champions = new ArrayList<>();
            map = (Map<String, Object>) ((Map<String, Object>) JSON.parse(LoLin1DataProviderUtils
                    .performRiotGet(DataUpdater.ALL_CHAMPIONS_URL.replace(
                            DataUpdater.REALM_PLACE_HOLDER, realm.getName().toLowerCase()).replace(
                            DataUpdater.LOCALE_PLACE_HOLDER, locale))))
                    .get("data");
        } catch (NullPointerException ex) {
            // No internet, so wait and retry
            try {
                Thread.sleep(DataUpdater.getRetryDelayMillis());
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            DataUpdater.performUpdate(realm, locale, newVersion);
            return;
        }
        for (String key : map.keySet()) {
            final Champion thisChampion = new Champion(
                    (HashMap<String, Object>) map.get(key));
            champions.add(thisChampion);
        }
        StringBuffer data = new StringBuffer("{\"status\":\"ok\", \"list\":");
        data.append(DataAccessObject.formatChampionListAsJSON(champions));
        data.append("}");
        ListManager.getInstance().setChampions(realm, locale, data.toString());
        DataAccessObject.setChampionsVersion(realm, newVersion);
        DataUpdater.UPDATING = Boolean.FALSE;
    }

    @SuppressWarnings("unchecked")
    private static String retrieveDragonMagicVersion(Region realm) {
        String ret, wholeFile = LoLin1DataProviderUtils
                .performRiotGet(DataUpdater.REALM_FILE_URL.replace(
                        DataUpdater.REALM_PLACE_HOLDER, realm.getName().toLowerCase()));
        Map<String, Object> realmJson = (Map<String, Object>) JSON
                .parse(wholeFile);
        String version;
        try {
            version = ((HashMap<String, String>) realmJson
                    .get(DataUpdater.INFO_WRAPPER))
                    .get(DataUpdater.VERSION_KEY);
        } catch (NullPointerException ex) {
            // There was trouble with the connection, so wait and retry
            try {
                Thread.sleep(DataUpdater.getRetryDelayMillis());
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            return DataUpdater.retrieveDragonMagicVersion(realm);
        }
        if (version != null) {
            DataAccessObject.putCDN(realm, realmJson.get(DataUpdater.CDN_KEY)
                    .toString());
        }
        ret = (version == null) ? "" : version;
        return ret;
    }

    public static void updateData() {
        for (Region realm : DataAccessObject.getSupportedRealms().keySet()) {
            String newVersion;
            if (!DataAccessObject.getVersion(realm)
                    .contentEquals(
                            (newVersion = DataUpdater
                                    .retrieveDragonMagicVersion(realm))
                    )) {
                for (String locale : DataAccessObject.getSupportedRealms().get(
                        realm)) {
                    DataUpdater.performUpdate(realm, locale, newVersion);
                }
                DataAccessObject.setChampionsVersion(realm, newVersion);
            }
        }
    }
}
